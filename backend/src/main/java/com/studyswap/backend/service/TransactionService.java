package com.studyswap.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.studyswap.backend.dto.TransactionResponseDTO;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.Transaction;
import com.studyswap.backend.model.TransactionStatus;
import com.studyswap.backend.model.TransactionType;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.MaterialRepository;
import com.studyswap.backend.dto.MaterialDTO;
import com.studyswap.backend.dto.MaterialRequestDTO;
import com.studyswap.backend.repository.TransactionRepository;

import jakarta.transaction.Transactional;


@Service
public class TransactionService {
	private final TransactionRepository transactionRepository;
	private final MaterialRepository materialRepository;

	private static final String MATERIAL_NOT_FOUND = "Material não encontrado";
		
	public TransactionService(TransactionRepository transactionRepository, MaterialRepository materialRepository) {
		this.transactionRepository = transactionRepository;
		this.materialRepository = materialRepository;
	}

	@Transactional
	public TransactionResponseDTO createTransaction(Authentication auth, Long idMaterial, MaterialRequestDTO offeredMaterialDTO) {
		Material material = materialRepository.findById(idMaterial).orElseThrow(
				()-> new ResponseStatusException(
						HttpStatus.NOT_FOUND, MATERIAL_NOT_FOUND)
		);
		User  receiver = (User) auth.getPrincipal();
		User announcer = material.getUser();
		TransactionType type = material.getTransactionType();
		
		if(announcer.equals(receiver)) {
		    throw new ResponseStatusException(
		            HttpStatus.BAD_REQUEST, "Não podes iniciar transação consigo mesmo");
		}
		
		if(!material.isAvailable()) {
			throw new ResponseStatusException(	
                    HttpStatus.BAD_REQUEST, "Material indisponível");
        }
		Transaction transaction;

		if (type == TransactionType.TROCA) {
			if (offeredMaterialDTO == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "É necessário enviar um material para realizar a troca");
			}
			Material offered = new Material();
			offered.setTitle(offeredMaterialDTO.getTitle());
			offered.setDescription(offeredMaterialDTO.getDescription());
			offered.setConservationStatus(offeredMaterialDTO.getConservationStatus());
			offered.setMaterialType(offeredMaterialDTO.getMaterialType());
			offered.setTransactionType(TransactionType.TROCA);
			offered.setAvailable(false);
			offered.setUser(receiver);

			transaction = new Transaction(material, announcer, receiver, TransactionStatus.PENDING, type, offered);
		} else {
			transaction = new Transaction(material, announcer, receiver, TransactionStatus.PENDING, type);
    }
		return convertToResponseDTO(transactionRepository.save(transaction));
	}
	
	@Transactional
	public void cancelTransaction(Authentication auth, Long idMaterial) {
		User user = (User) auth.getPrincipal();

		Material material = materialRepository.findById(idMaterial)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, MATERIAL_NOT_FOUND));

		List<Transaction> transactions = transactionRepository.findByMaterial(material);

		Transaction transaction = transactions.stream()
				.filter(t -> isParticipantInTransaction(user, t))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.FORBIDDEN, "Usuário não autorizado a cancelar esta transação"));

		if (transaction.getStatus() != TransactionStatus.PENDING) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível cancelar transação que não está pendente");
		}

		transactionRepository.delete(transaction);
	}

	
    @Transactional
    public TransactionResponseDTO completeTransaction(Authentication auth, Long idTransaction){
    	User loggedUser = (User) auth.getPrincipal();
    	Transaction transaction = transactionRepository.findById(idTransaction).orElseThrow(
    			()-> new ResponseStatusException(
    					HttpStatus.NOT_FOUND, "Transação não encontrada")
    			);
    	if(!isAnnouncer(loggedUser, transaction)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas o dono do material pode confirmar a transação");
    	}
    	if(transaction.getStatus()!=TransactionStatus.PENDING) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Só pode-se confirmar transação pendente");
    	}
    	transaction.getMaterial().setAvailable(false);
		transaction.setStatus(TransactionStatus.CONCLUDED);

		transactionRepository.findByMaterial(transaction.getMaterial()).stream()
		.filter(t -> !t.getId().equals(transaction.getId()) && t.getStatus() == TransactionStatus.PENDING)
		.forEach(t -> {
			t.setStatus(TransactionStatus.DENIED);
			transactionRepository.save(t);
		});

    	return convertToResponseDTO(transactionRepository.save(transaction));
    }
    
    public List<TransactionResponseDTO> findAllTransactionsByMaterial(Authentication auth, Long idMaterial) {
    	User loggedUser = (User) auth.getPrincipal();

		Material material = materialRepository.findById(idMaterial).orElseThrow(
				()-> new ResponseStatusException(
						HttpStatus.NOT_FOUND, MATERIAL_NOT_FOUND)
		);    	
		if(!material.getUser().getId().equals(loggedUser.getId())) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas o dono do material pode ver todas suas transações");
    	}
        List<Transaction> transactions = transactionRepository.findByMaterial(material);
        return transactions.stream().map(this::convertToResponseDTO).toList();
    }

	public List<TransactionResponseDTO> findAllTransactionsByUser(Authentication auth) {
		User loggedUser = (User) auth.getPrincipal();
		List<Transaction> transactions = transactionRepository.findByAnnouncer(loggedUser);
		transactions.addAll(transactionRepository.findByReceiver(loggedUser));
		return transactions.stream().map(this::convertToResponseDTO).toList();
	}
    
    private TransactionResponseDTO convertToResponseDTO(Transaction transaction) {
		MaterialDTO offeredMaterial = null;
		
		if (transaction.getMaterialTrade() != null) {
			offeredMaterial = new MaterialDTO(
				transaction.getMaterialTrade().getId(),
				transaction.getMaterialTrade().getTitle(), 
				transaction.getMaterialTrade().getMaterialType(),
				transaction.getMaterialTrade().getConservationStatus()
			);
		}

		return new TransactionResponseDTO(
			transaction.getId(),
			transaction.getTransactionDate(),
			transaction.getMaterial().getId(),
			transaction.getMaterial().getTitle(),
			transaction.getStatus(),
			transaction.getReceiver().getId(),
			transaction.getReceiver().getName(),
			transaction.getAnnouncer().getId(),
			transaction.getAnnouncer().getName(),
			transaction.getType(),
			offeredMaterial
		);
	}

	private boolean isParticipantInTransaction(User user, Transaction transaction) {
		return isAnnouncer(user, transaction) || isReceiver(user, transaction);
	}

	private boolean isAnnouncer(User user, Transaction transaction){
		return transaction.getAnnouncer() != null && user != null
				&& transaction.getAnnouncer().getId() != null
				&& transaction.getAnnouncer().getId().equals(user.getId());
	}
	
	private boolean isReceiver(User user, Transaction transaction){
		return transaction.getReceiver() != null && user != null
				&& transaction.getReceiver().getId() != null
				&& transaction.getReceiver().getId().equals(user.getId());
	}
}
