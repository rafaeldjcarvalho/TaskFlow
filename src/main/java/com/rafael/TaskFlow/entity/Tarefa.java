package com.rafael.TaskFlow.entity;

import com.rafael.TaskFlow.entity.enums.Prioridade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "tarefas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tarefa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@NotEmpty
	@Column(nullable = false, length = 50)
	private String titulo;
	
	@Column(nullable = true, length = 100)
	private String descricao;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Prioridade prioridade;
	
	private int ordem;
	
	@ManyToOne
	@JoinColumn(name = "coluna_id")
	private Coluna coluna;
	
}
