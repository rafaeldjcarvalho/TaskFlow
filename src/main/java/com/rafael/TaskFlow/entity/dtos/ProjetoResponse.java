package com.rafael.TaskFlow.entity.dtos;

import java.util.List;

public record ProjetoResponse(
		Long id,
		String nome,
		Long usuario_id,
		List<ColunaDTO> colunas) {}
