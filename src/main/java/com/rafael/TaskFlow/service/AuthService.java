package com.rafael.TaskFlow.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rafael.TaskFlow.entity.Usuario;
import com.rafael.TaskFlow.entity.dtos.LoginRequestDTO;
import com.rafael.TaskFlow.entity.dtos.LoginResponseDTO;
import com.rafael.TaskFlow.infra.security.TokenService;
import com.rafael.TaskFlow.repository.UsuarioRepository;

@Service
public class AuthService {

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private TokenService tokenService;
	
	public LoginResponseDTO login(LoginRequestDTO data) {
		Usuario user = repository.findByEmail(data.email()).orElseThrow(() -> new RuntimeException("User not found"));
		if(passwordEncoder.matches(data.senha(), user.getSenha())) {
            String token = this.tokenService.generateToken(user);
            return new LoginResponseDTO(user.getEmail(), token);
        }
		return new LoginResponseDTO(null, null);
	}
	
	public LoginResponseDTO registrar(LoginRequestDTO data) {
		Optional<Usuario> user = this.repository.findByEmail(data.email());

		if(user.isEmpty()) {
			Usuario newUser = new Usuario();
			newUser.setSenha(passwordEncoder.encode(data.senha()));
			newUser.setEmail(data.email());
			this.repository.save(newUser);

			return new LoginResponseDTO(newUser.getEmail(), null);
		}
		return new LoginResponseDTO(null, null);
	}
}
