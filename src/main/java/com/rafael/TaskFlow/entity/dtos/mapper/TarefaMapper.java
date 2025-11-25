package com.rafael.TaskFlow.entity.dtos.mapper;

import org.springframework.stereotype.Component;

import com.rafael.TaskFlow.entity.Tarefa;
import com.rafael.TaskFlow.entity.dtos.TarefaDTO;

@Component
public class TarefaMapper {
	
	public TarefaDTO toDTO(Tarefa data) {
		if(data == null) {
			return null;
		}
		return new TarefaDTO(
				data.getId(), 
				data.getTitulo(), 
				data.getDescricao(), 
				data.getPrioridade(), 
				data.getOrdem());
	}
}
