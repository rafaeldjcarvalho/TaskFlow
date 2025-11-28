package com.rafael.TaskFlow.service;

import static org.assertj.core.api.Assertions.assertThat;
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

import com.rafael.TaskFlow.entity.Coluna;
import com.rafael.TaskFlow.entity.Projeto;
import com.rafael.TaskFlow.entity.Tarefa;
import com.rafael.TaskFlow.entity.Usuario;
import com.rafael.TaskFlow.entity.dtos.TarefaDTO;
import com.rafael.TaskFlow.entity.enums.Prioridade;
import com.rafael.TaskFlow.repository.ColunaRepository;
import com.rafael.TaskFlow.repository.TarefaRepository;

public class TarefaServiceTest {
	
	@Mock
	private TarefaRepository tarefaRepository;
	
	@Mock
	private ColunaRepository colunaRepository;
	
	@Autowired
	@InjectMocks
	private TarefaService tarefaService;
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	@DisplayName("Deve criar uma tarefa com sucesso no BD")
	public void createTaskCase1() {
		TarefaDTO data = new TarefaDTO(1l, "titulo", "desc", Prioridade.ALTA, 1);
		Coluna coluna = new Coluna(1l, "titulo", 0, new Projeto(1l, "projeto", new Usuario(1l, "email@gmail.com", "123123123")));
		
		when(colunaRepository.findById(coluna.getId())).thenReturn(Optional.of(coluna));
		when(tarefaRepository.save(any(Tarefa.class))).thenReturn(new Tarefa(data.id(), data.titulo(), data.descricao(), data.prioridade(), data.ordem(), coluna));
		
		TarefaDTO result = this.tarefaService.createTask(data, 1l, 1l);
		
		verify(colunaRepository, times(1)).findById(coluna.getId());
		verify(tarefaRepository, times(1)).save(any(Tarefa.class));
		
		assertThat(result.id()).isEqualTo(data.id());
		assertThat(result.titulo()).isEqualTo(data.titulo());
		assertThat(result.descricao()).isEqualTo(data.descricao());
		assertThat(result.prioridade()).isEqualTo(data.prioridade());
		assertThat(result.ordem()).isEqualTo(data.ordem());
	}
	
	@Test
	public void createTaskCase2() {
		
	}
	
	@Test
	public void createTaskCase3() {
		
	}

}
