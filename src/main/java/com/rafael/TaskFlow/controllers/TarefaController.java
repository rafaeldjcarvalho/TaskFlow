package com.rafael.TaskFlow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.TaskFlow.entity.dtos.MoverTarefaDTO;
import com.rafael.TaskFlow.entity.dtos.TarefaDTO;
import com.rafael.TaskFlow.service.TarefaService;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

	@Autowired
	private TarefaService service;
	
	@PostMapping("/column/{column_id}")
	public ResponseEntity<TarefaDTO> createTask(@RequestBody TarefaDTO data, @PathVariable Long column_id) {
		TarefaDTO result = service.createTask(data, column_id);
		return ResponseEntity.ok(result);
	}
	
	@PutMapping("/{task_id}")
	public ResponseEntity<TarefaDTO> updateTask(@PathVariable Long task_id, @RequestBody TarefaDTO data) {
		TarefaDTO result = service.updateTask(task_id, data);
		return ResponseEntity.ok(result);
	}
	
	@PutMapping("/{task_id}/mover")
	public ResponseEntity<TarefaDTO> moveTask(@PathVariable Long task_id, @RequestBody MoverTarefaDTO data) {
		TarefaDTO result = service.moveTask(task_id, data);
		return ResponseEntity.ok(result);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		service.deleteTask(id);
		return ResponseEntity.noContent().build();
	}
}
