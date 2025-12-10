package com.rafael.TaskFlow.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.TaskFlow.entity.Usuario;
import com.rafael.TaskFlow.entity.dtos.ProjetoRequest;
import com.rafael.TaskFlow.entity.dtos.ProjetoResponse;
import com.rafael.TaskFlow.service.ProjetoService;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {
	
	@Autowired
	private ProjetoService service;
	
	@GetMapping
	public ResponseEntity<List<ProjetoResponse>> listAllProjects(@AuthenticationPrincipal Usuario usuarioLogado) {
		List<ProjetoResponse> lista = service.getAllProjects(usuarioLogado.getId());
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProjetoResponse> projectById(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
		ProjetoResponse result = service.getProjectById(id, usuarioLogado.getId());
		return ResponseEntity.ok(result);
	}
	
	@PostMapping
	public ResponseEntity<ProjetoResponse> createProject(@RequestBody ProjetoRequest data, @AuthenticationPrincipal Usuario usuarioLogado) {
		ProjetoResponse projeto = service.createNewProject(data, usuarioLogado.getId());
		return ResponseEntity.ok(projeto);
	}

}
