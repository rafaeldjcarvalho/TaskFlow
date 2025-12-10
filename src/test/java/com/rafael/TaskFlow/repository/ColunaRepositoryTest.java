package com.rafael.TaskFlow.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.rafael.TaskFlow.entity.Coluna;
import com.rafael.TaskFlow.entity.Projeto;
import com.rafael.TaskFlow.entity.Usuario;
import com.rafael.TaskFlow.entity.dtos.ProjetoRequest;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class ColunaRepositoryTest {
	
	@Autowired
	private ColunaRepository colunaRepository;
	
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
	
	private Projeto createProject(ProjetoRequest data, Long usuario_id) {
		Projeto project = new Projeto();
		project.setNome(data.nome());
		project.setUsuario(usuarioRepository.findById(usuario_id).get());
		this.entityManager.persist(project);
		return project;
	}
	
	private Coluna createColumn(String titulo, int ordem, Long project_id) {
		Coluna column = new Coluna();
		column.setTitulo(titulo);
		column.setOrdem(ordem);
		column.setProjeto(projetoRepository.findById(project_id).get());
		this.entityManager.persist(column);
		return column;
	}
	
	@Test
	@DisplayName("Deve receber uma lista de colunas com sucesso do BD")
	public void findColunasByProjetoCase1() {
		Usuario usuario = this.createUser(new Usuario(null, "usuario@gmail.com", "123123123"));
		Projeto projeto = this.createProject(new ProjetoRequest("Projeto1"), usuario.getId());
		
		Coluna coluna1 = this.createColumn("A fazer", 1, projeto.getId());
		Coluna coluna2 = this.createColumn("Fazendo", 2, projeto.getId());
		Coluna coluna3 = this.createColumn("Concluido", 3, projeto.getId());
		
		List<Coluna> result = colunaRepository.findColunasByProjeto(projeto.getId());
		
		assertThat(result.size()).isEqualTo(3);
		assertThat(result.contains(coluna1)).isTrue();
		assertThat(result.contains(coluna2)).isTrue();
		assertThat(result.contains(coluna3)).isTrue();
	}
	
	@Test
	@DisplayName("Nao deve receber uma lista de colunas, quando o projeto nao existe")
	public void findColunasByProjetoCase2() {
		List<Coluna> result = colunaRepository.findColunasByProjeto(4l);
		
		assertThat(result.size()).isEqualTo(0);
		assertThat(result.isEmpty()).isTrue();
	}

}
