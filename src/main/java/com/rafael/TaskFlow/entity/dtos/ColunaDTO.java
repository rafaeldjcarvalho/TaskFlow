package com.rafael.TaskFlow.entity.dtos;

import java.util.List;

public record ColunaDTO(
		Long id,
		String titulo,
		int ordem,
		List<TarefaDTO> tarefas) {}
