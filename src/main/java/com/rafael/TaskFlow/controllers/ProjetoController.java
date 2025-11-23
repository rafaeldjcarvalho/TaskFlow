package com.rafael.TaskFlow.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.TaskFlow.entity.Projeto;
import com.rafael.TaskFlow.entity.dtos.ProjetoRequest;
import com.rafael.TaskFlow.entity.dtos.ProjetoResponse;
import com.rafael.TaskFlow.service.ProjetoService;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {
	
	@Autowired
	private ProjetoService service;
	private Long id_usuario = 1l;
	
	@GetMapping
	public ResponseEntity<List<Projeto>> listarProjetos() {
		List<Projeto> lista = service.getAllProjects(id_usuario);
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProjetoResponse> projetoPorId(@PathVariable Long id) {
		ProjetoResponse result = service.getProjectById(id, id_usuario);
		return ResponseEntity.ok(result);
	}
	
	@PostMapping
	public ResponseEntity<Projeto> criarProjeto(@RequestBody ProjetoRequest data) {
		Projeto projeto = service.createNewProject(data);
		return ResponseEntity.ok(projeto);
	}

}
