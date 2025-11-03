package com.rafael.TaskFlow.entity.dtos;

import com.rafael.TaskFlow.entity.enums.Prioridade;

public record TarefaDTO(
		Long id,
		String titulo,
		String descricao,
		Prioridade prioridade,
		int ordem) {}
