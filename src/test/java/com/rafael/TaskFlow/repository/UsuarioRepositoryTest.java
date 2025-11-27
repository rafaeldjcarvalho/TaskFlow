package com.rafael.TaskFlow.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.rafael.TaskFlow.entity.Usuario;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired 
	private EntityManager entityManager;
	
	private Usuario createUsuario(Usuario data) {
		Usuario user = new Usuario(data.getId(), data.getEmail(), data.getSenha());
		this.entityManager.persist(user);
		return user;
	}
	
	@Test
	@DisplayName("Deve receber um usuario com sucesso do BD")
	public void findByEmailCase1() {
		String email = "teste@gmail.com";
		Usuario usuario = new Usuario(null, email, "12345678");
		this.createUsuario(usuario);
		
		Optional<Usuario> result = this.usuarioRepository.findByEmail(email);
		
		assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	@DisplayName("Nao deve receber um usuario, quando o usuario nao existe")
	public void findByEmailCase12() {
		String email = "teste@gmail.com";
		
		Optional<Usuario> result = this.usuarioRepository.findByEmail(email);
		
		assertThat(result.isEmpty()).isTrue();
	}

}
