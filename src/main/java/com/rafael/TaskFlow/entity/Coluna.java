package com.rafael.TaskFlow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "colunas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coluna {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@NotEmpty
	@Column(nullable = false, length = 50)
	private String titulo;
	
	@Column(nullable = false)
	private int ordem;
	
	@ManyToOne
	@JoinColumn(name = "projeto_id")
	private Projeto projeto;
}
