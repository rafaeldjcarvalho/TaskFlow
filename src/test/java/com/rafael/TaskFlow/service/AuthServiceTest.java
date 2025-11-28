package com.rafael.TaskFlow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rafael.TaskFlow.entity.Usuario;
import com.rafael.TaskFlow.entity.dtos.LoginRequestDTO;
import com.rafael.TaskFlow.entity.dtos.LoginResponseDTO;
import com.rafael.TaskFlow.infra.security.TokenService;
import com.rafael.TaskFlow.repository.UsuarioRepository;

public class AuthServiceTest {
	
	@Mock
	private UsuarioRepository repository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private TokenService tokenService;
	
	@Autowired
	@InjectMocks
	private AuthService authService;
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	@DisplayName("Deve logar com sucesso")
	public void loginCase1() {
		LoginRequestDTO data = new LoginRequestDTO("teste@gmail.com", "123123123");
		
		when(repository.findByEmail(data.email())).thenReturn(Optional.of(new Usuario(1l, data.email(), data.senha())));
		when(passwordEncoder.matches(any(), any())).thenReturn(true);
		when(tokenService.generateToken(any(Usuario.class))).thenReturn("token");
		
		LoginResponseDTO result = this.authService.login(data);
		
		verify(repository, times(1)).findByEmail(data.email());
		verify(passwordEncoder, times(1)).matches(any(), any());
		verify(tokenService, times(1)).generateToken(any(Usuario.class));
		
		assertThat(result).isNotNull();
		assertThat(result.email()).isEqualTo(data.email());
		assertThat(result.token()).isEqualTo("token");
	}
	
	@Test
	@DisplayName("Nao pode logar, quando o usuario nao existe")
	public void loginCase2() {
		LoginRequestDTO data = new LoginRequestDTO("naoexiste@gmail.com", "123123123");
		
		Exception thrown = assertThrows(RuntimeException.class, () -> {
			this.authService.login(data);
		});
		
		assertEquals("User not found", thrown.getMessage());
	}
	
	@Test
	@DisplayName("Nao pode logar, quando a senha esta incorreta")
	public void loginCase3() {
		LoginRequestDTO data = new LoginRequestDTO("teste@gmail.com", "123123123");
		
		when(repository.findByEmail(data.email())).thenReturn(Optional.of(new Usuario(1l, data.email(), data.senha())));
		when(passwordEncoder.matches(any(), any())).thenReturn(false);
		
		LoginResponseDTO result = this.authService.login(data);
		
		assertThat(result.email()).isNull();
		assertThat(result.token()).isNull();
	}

	@Test
	@DisplayName("Deve criar um novo usuario com sucesso")
	public void registrarCase1() {
		LoginRequestDTO data = new LoginRequestDTO("teste@gmail.com", "123123123");
		
		when(repository.findByEmail(data.email())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(data.senha())).thenReturn("criptografada");
		when(repository.save(any(Usuario.class))).thenReturn(new Usuario());
		
		LoginResponseDTO result = this.authService.registrar(data);
		
		verify(repository, times(1)).findByEmail(data.email());
		verify(passwordEncoder, times(1)).encode(data.senha());
		verify(repository, times(1)).save(any(Usuario.class));
		
		assertThat(result.email()).isEqualTo(data.email());
		assertThat(result.token()).isNull();
	}
	
	@Test
	@DisplayName("Nao deve criar um usuario, quando ele ja esta registrado")
	public void registrarCase2() {
		LoginRequestDTO data = new LoginRequestDTO("teste@gmail.com", "123123123");
		
		when(repository.findByEmail(data.email())).thenReturn(Optional.of(new Usuario()));
		
		LoginResponseDTO result = this.authService.registrar(data);
		
		verify(repository, times(1)).findByEmail(data.email());
		
		assertThat(result.email()).isNull();
		assertThat(result.token()).isNull();
	}
}
