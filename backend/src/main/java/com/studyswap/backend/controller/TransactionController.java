package com.studyswap.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studyswap.backend.dto.TransactionResponseDTO;
import com.studyswap.backend.service.TransactionService;


@RestController
@RequestMapping("/transactions")
public class TransactionController {
	private final TransactionService transactionService;
	
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@DeleteMapping("/{idTransaction}")
	public ResponseEntity<Void> cancelTransaction(Authentication auth, @PathVariable("idTransaction") Long idTransaction){
		transactionService.cancelTransaction(auth, idTransaction);
		return ResponseEntity.noContent().build();		
	}
	@PostMapping("/material/{idMaterial}")
	public ResponseEntity<TransactionResponseDTO>createTransaction(Authentication auth, @PathVariable("idMaterial") Long idMaterial){
		return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(auth, idMaterial));
	}
	@PutMapping("/{idTransaction}")
	public ResponseEntity<TransactionResponseDTO> completeTransaction(Authentication auth, 
			@PathVariable("idTransaction") Long idTransaction){
		return ResponseEntity.status(HttpStatus.OK).body(
				transactionService.completeTransaction(auth, idTransaction));
	}
	
	@GetMapping("/material/{idMaterial}")
	public ResponseEntity<List<TransactionResponseDTO>> findAllTransactionsByMaterial(Authentication auth, 
			@PathVariable("idMaterial") Long idMaterial){
		return ResponseEntity.status(HttpStatus.OK).body(
				transactionService.findAllTransactionsByMaterial(auth, idMaterial));
	}

}

