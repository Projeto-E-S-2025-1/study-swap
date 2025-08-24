package com.studyswap.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import com.studyswap.backend.dto.TransactionResponseDTO;
import com.studyswap.backend.model.Material;
import com.studyswap.backend.model.Transaction;
import com.studyswap.backend.model.TransactionStatus;
import com.studyswap.backend.model.User;
import com.studyswap.backend.repository.MaterialRepository;
import com.studyswap.backend.repository.TransactionRepository;

import jakarta.transaction.Transactional;



public class TransactionService {
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	private MaterialRepository materialRepository;
		
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
		
		material.setAvailable(false);
		Transaction transaction = new Transaction(material, announcer,
receiver, TransactionStatus.PENDING	);
		return convertToResponseDTO(transactionRepository.save(transaction));
	}
	@Transactional
	public void CancelTransaction(Authentication auth, Long idTransaction) {
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
		if(!(transaction.getStatus()==TransactionStatus.PENDING)){
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
		transaction.setStatus(TransactionStatus.CONCLUDED);
    	return convertToResponseDTO(transactionRepository.save(transaction));
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
		return transaction.getAnnouncer().getId().equals(user.getId());
	}
	private boolean isReceiver(User user, Transaction transaction) {
		return transaction.getReceiver().getId().equals(user.getId());
	}
}
