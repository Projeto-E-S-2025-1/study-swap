package com.studyswap.backend.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.studyswap.backend.dto.CreateQuestionDTO;
import com.studyswap.backend.dto.QuestionResponseDTO;
import com.studyswap.backend.dto.UpdateQuestionDTO;
import com.studyswap.backend.model.Question;
import com.studyswap.backend.service.QuestionService;
@RestController
@RequestMapping("/questions")
public class QuestionController{
	
	private final QuestionService questionService;

	public QuestionController(QuestionService questionService) {
		this.questionService = questionService;
	}

	@PostMapping
	public ResponseEntity<Question> createQuestion(@RequestBody CreateQuestionDTO dto, Authentication auth) {
	    return ResponseEntity.status(HttpStatus.CREATED).body(questionService.createQuestion(dto, auth));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Question> updateQuestion(@PathVariable("id") Long questionId, 
			@RequestBody UpdateQuestionDTO dto, Authentication auth){	

		return ResponseEntity.status(HttpStatus.OK).body(questionService.updateQuestion(questionId, dto, auth));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteQuestion(@PathVariable("id") Long questionId, Authentication auth){
		questionService.deleteQuestion(questionId, auth);
		return ResponseEntity.noContent().build();		
	}
	@GetMapping("/material/{id}")
	public	ResponseEntity<List<QuestionResponseDTO>> getQuestionsByMaterial(@PathVariable("id") Long materialId){
		return ResponseEntity.status(HttpStatus.OK).body(questionService.getQuestionsByMaterial(materialId));
	}
	
}