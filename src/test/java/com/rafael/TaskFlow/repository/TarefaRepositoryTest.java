package com.rafael.TaskFlow.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.rafael.TaskFlow.entity.Coluna;
import com.rafael.TaskFlow.entity.Projeto;
import com.rafael.TaskFlow.entity.Tarefa;
import com.rafael.TaskFlow.entity.Usuario;
import com.rafael.TaskFlow.entity.dtos.ProjetoRequest;
import com.rafael.TaskFlow.entity.enums.Prioridade;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class TarefaRepositoryTest {
	
	@Autowired
	private TarefaRepository tarefaRepository;

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
	
	private Tarefa createTask(String titulo, String descricao, Prioridade prioridade, int ordem, Long coluna_id) {
		Tarefa task = new Tarefa();
		task.setTitulo(titulo);
		task.setDescricao(descricao);
		task.setPrioridade(prioridade);
		task.setOrdem(ordem);
		task.setColuna(colunaRepository.findById(coluna_id).get());
		this.entityManager.persist(task);
		return task;
	}
	
	@Test
	@DisplayName("Deve receber uma lista de tarefa com sucesso do BD")
	public void findTarefasByColunaCase1() {
		Usuario usuario = this.createUser(new Usuario(null, "teste@gmail.com", "123123123"));
		Projeto projeto = this.createProject(new ProjetoRequest("Projeto1"), usuario.getId());
		Coluna coluna = this.createColumn("A fazer", 1, projeto.getId());
		
		Tarefa tarefa1 = this.createTask("Estudar", null, Prioridade.ALTA, 1, coluna.getId());
		Tarefa tarefa2 = this.createTask("Jogar", null, Prioridade.ALTA, 2, coluna.getId());
		
		List<Tarefa> result = tarefaRepository.findTarefasByColuna(coluna.getId());
		
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.contains(tarefa1)).isTrue();
		assertThat(result.contains(tarefa2)).isTrue();
	}
	
	@Test
	@DisplayName("Nao deve receber uma lista de tarefas, quando a coluna nao existe")
	public void findTarefasByColunaCase2() {
		List<Tarefa> result = tarefaRepository.findTarefasByColuna(5l);
		
		assertThat(result.size()).isEqualTo(0);
		assertThat(result.isEmpty()).isTrue();
	}
	
	@Test
	@DisplayName("Deve retornar uma tarefa com sucesso do BD")
	public void findByOrdemCase1() {
		Usuario usuario = this.createUser(new Usuario(null, "teste@gmail.com", "123123123"));
		Projeto projeto = this.createProject(new ProjetoRequest("Projeto1"), usuario.getId());
		Coluna coluna = this.createColumn("A fazer", 1, projeto.getId());
		
		this.createTask("Estudar", null, Prioridade.ALTA, 1, coluna.getId());
		
		Optional<Tarefa> result = tarefaRepository.findByOrdem(1, coluna.getId());
		
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getOrdem()).isEqualTo(1);
	}
	
	@Test
	@DisplayName("Nao deve retornar uma tarefa, quando a ordem nao existe")
	public void findByOrdemCase2() {
		Optional<Tarefa> result = tarefaRepository.findByOrdem(1, 1l);
		
		assertThat(result.isEmpty()).isTrue();
	}
}
