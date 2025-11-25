package com.rafael.TaskFlow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.TaskFlow.entity.dtos.LoginRequestDTO;
import com.rafael.TaskFlow.entity.dtos.LoginResponseDTO;
import com.rafael.TaskFlow.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService service;
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO data) {
		LoginResponseDTO response = service.login(data);
		if(response.email().equals(null) && response.token().equals(null)) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/registrar")
	public ResponseEntity<LoginResponseDTO> register(@RequestBody LoginRequestDTO data) {
		LoginResponseDTO response = service.registrar(data);
		if(response.email().equals(null)) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(response);
	}
}
