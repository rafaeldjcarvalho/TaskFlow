package com.rafael.TaskFlow.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.rafael.TaskFlow.entity.Projeto;
import com.rafael.TaskFlow.entity.Usuario;
import com.rafael.TaskFlow.entity.dtos.ProjetoRequest;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class ProjetoRepositoryTest {
	
	@Autowired
	private ProjetoRepository projetoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	private Usuario createUser(Usuario data) {
		Usuario user = new Usuario(data.getId(), data.getEmail(), data.getSenha());
		this.entityManager.persist(user);
		return user;
	}
	
	private Projeto createProject(ProjetoRequest data) {
		Projeto project = new Projeto();
		project.setNome(data.nome());
		project.setUsuario(usuarioRepository.findById(data.usuario_id()).get());
		this.entityManager.persist(project);
		return project;
	}
	
	@Test
	@DisplayName("Deve receber uma lista de projetos com sucesso do BD")
	public void findProjetosByUsuarioCase1() {
		Usuario usuario = this.createUser(new Usuario(null, "usuario@gmail.com", "12345678"));
		Projeto projeto1 = this.createProject(new ProjetoRequest("Projeto1", usuario.getId()));
		Projeto projeto2 = this.createProject(new ProjetoRequest("Projeto2", usuario.getId()));
		
		List<Projeto> result = this.projetoRepository.findProjetosByUsuario(usuario.getId());
		
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0)).isEqualTo(projeto1);
		assertThat(result.get(1)).isEqualTo(projeto2);
	}
	
	@Test
	@DisplayName("Nao deve receber uma lista de projetos, quando o usuario nao existe")
	public void findProjetosByUsuarioCase2() {
		List<Projeto> result = this.projetoRepository.findProjetosByUsuario(2l);
		
		assertThat(result.size()).isEqualTo(0);
		assertThat(result.isEmpty()).isTrue();
	}

}
