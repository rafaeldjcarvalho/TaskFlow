package com.rafael.TaskFlow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rafael.TaskFlow.entity.Coluna;
import com.rafael.TaskFlow.entity.Tarefa;
import com.rafael.TaskFlow.entity.dtos.MoverTarefaDTO;
import com.rafael.TaskFlow.entity.dtos.TarefaDTO;
import com.rafael.TaskFlow.repository.ColunaRepository;
import com.rafael.TaskFlow.repository.TarefaRepository;

@Service
public class TarefaService {
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	@Autowired
	private ColunaRepository colunaRepository;
	
	public TarefaDTO createTask(TarefaDTO data, Long column_id) {
		Coluna aFazer = colunaRepository.findById(column_id).orElseThrow(() -> new RuntimeException("Column not found"));
		Tarefa novaTarefa = new Tarefa();
		novaTarefa.setTitulo(data.titulo());
		novaTarefa.setDescricao(data.descricao());
		novaTarefa.setPrioridade(data.prioridade());
		novaTarefa.setOrdem(data.ordem());
		novaTarefa.setColuna(aFazer);
		
		Tarefa tarefaSalva = this.saveTask(novaTarefa);
		return new TarefaDTO(tarefaSalva.getId(), tarefaSalva.getTitulo(), tarefaSalva.getDescricao(), tarefaSalva.getPrioridade(), tarefaSalva.getOrdem());
	}
	
	public TarefaDTO updateTask(Long task_id, TarefaDTO new_data) {
		Tarefa tarefa = tarefaRepository.findById(task_id).orElseThrow(() -> new RuntimeException("Task not found"));
		tarefa.setTitulo(new_data.titulo());
		tarefa.setDescricao(new_data.descricao());
		tarefa.setPrioridade(new_data.prioridade());
		
		Tarefa result = this.saveTask(tarefa);
		return new TarefaDTO(result.getId(), result.getTitulo(), result.getDescricao(), result.getPrioridade(), result.getOrdem());
		
	}
	
	public void deleteTask(Long task_id) {
		Tarefa tarefa = tarefaRepository.findById(task_id).orElseThrow(() -> new RuntimeException("Task not found"));
		tarefaRepository.delete(tarefa);
	}
	
	public TarefaDTO moveTask(Long task_id, MoverTarefaDTO data) {
		Tarefa tarefa = tarefaRepository.findById(task_id).orElseThrow(() -> new RuntimeException("Task not found"));
		Coluna novaColuna = colunaRepository.findById(data.idNovaColuna()).orElseThrow(() -> new RuntimeException("Column not found"));
		
		tarefa.setOrdem(data.idNovaOrdem());
		tarefa.setColuna(novaColuna);
		
		Tarefa moved = this.saveTask(tarefa);
		return new TarefaDTO(moved.getId(), moved.getTitulo(), moved.getDescricao(), moved.getPrioridade(), moved.getOrdem());
	}
	
	private Tarefa saveTask(Tarefa data) {
		return tarefaRepository.save(data);
	}

}
