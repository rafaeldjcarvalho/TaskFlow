package com.rafael.TaskFlow.entity.dtos.mapper;

import org.springframework.stereotype.Component;

import com.rafael.TaskFlow.entity.Projeto;
import com.rafael.TaskFlow.entity.dtos.ProjetoResponse;

@Component
public class ProjetoMapper {
	
	public ProjetoResponse toDTO(Projeto data) {
		if(data == null) {
			return null;
		}
		return new ProjetoResponse(
				data.getId(),
				data.getNome(),
				data.getUsuario().getId(),
				null);
	}

}
