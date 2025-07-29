package com.studyswap.backend.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Question {
	//Atributos	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable=false)
	private Long id;
	
	@Column(nullable=false)
	@NotBlank(message = "a descrição é obrigatória")
	private String description;
	
	@Column(nullable=false)
	@NotBlank(message = "o título é obrigatório")
	private String title;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User author;
	
	@ManyToOne
	@JoinColumn(name="material_id", nullable=false)
	private Material material;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		return Objects.equals(id, other.id);
	}

	public Question(Long id, String description, String title, User author, Material material) {
		this.id = id;
		this.description = description;
		this.title = title;
		this.author = author;
		this.material = material;
	}
	

}
