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
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.MaterialRepository;
import com.studyswap.backend.repository.TransactionRepository;

import jakarta.transaction.Transactional;


@Service
public class TransactionService {
	private final TransactionRepository transactionRepository;
	private final MaterialRepository materialRepository;
		
	public TransactionService(TransactionRepository transactionRepository, MaterialRepository materialRepository) {
		this.transactionRepository = transactionRepository;
		this.materialRepository = materialRepository;
	}

	@Transactional
	public TransactionResponseDTO createTransaction(Authentication auth, Long idMaterial){
		Material material = materialRepository.findById(idMaterial).orElseThrow(
				()-> new ResponseStatusException(
						HttpStatus.NOT_FOUND, "Material não encontrado")
		);
		User  receiver = (User) auth.getPrincipal();
		User announcer = material.getUser();
		
		if(announcer.equals(receiver)) {
		    throw new ResponseStatusException(
		            HttpStatus.BAD_REQUEST, "Não podes iniciar transação consigo mesmo");
		}
		
		if(!material.isAvailable()) {
			throw new ResponseStatusException(	
                    HttpStatus.BAD_REQUEST, "Material indisponível");
        }
		Transaction transaction = new Transaction(idMaterial, material, announcer,
receiver, TransactionStatus.PENDING	);
		return convertToResponseDTO(transactionRepository.save(transaction));
	}
	
	@Transactional
	public void cancelTransaction(Authentication auth, Long idTransaction) {
		User user = (User) auth.getPrincipal();
		//verifica se a transação está no BD
		Transaction transaction = transactionRepository.findById(idTransaction).orElseThrow(()
				-> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Transação não encontrada")
				);	
		//verifica se o usuário participa da transação
		if(!isParticipantInTransaction(user, transaction)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "usuário não autorizado");
		}
		//verifica se a transação está pendente
		if(transaction.getStatus()!=TransactionStatus.PENDING){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "não pode cancelar transação que não está pendente");
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
    	return convertToResponseDTO(transactionRepository.save(transaction));
    }
    
    public List<TransactionResponseDTO> findAllTransactionsByMaterial(Authentication auth, Long idMaterial) {
    	User loggedUser = (User) auth.getPrincipal();

		Material material = materialRepository.findById(idMaterial).orElseThrow(
				()-> new ResponseStatusException(
						HttpStatus.NOT_FOUND, "Material não encontrado")
		);    	
		if(!loggedUser.equals(material.getUser())) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas o dono do material pode ver todas suas transações");
    	}
        List<Transaction> transactions = transactionRepository.findByMaterial(material);
        return transactions.stream().map(this::convertToResponseDTO).toList();
    }
    
    private TransactionResponseDTO convertToResponseDTO(Transaction transaction) {
		return new TransactionResponseDTO(transaction.getId(), transaction.getTransactionDate(), transaction.getMaterial().getId(), 
				transaction.getStatus(), transaction.getReceiver().getId(), transaction.getReceiver().getName(),
				transaction.getAnnouncer().getId(), transaction.getAnnouncer().getName());
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
