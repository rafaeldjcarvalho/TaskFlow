package com.rafael.TaskFlow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.rafael.TaskFlow.entity.dtos.MoverTarefaDTO;
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
		when(tarefaRepository.findTarefasByColuna(coluna.getId())).thenReturn(new ArrayList<Tarefa>());
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
	@DisplayName("Nao deve criar uma tarefa, quando a coluna nao existe")
	public void createTaskCase2() {
		Exception thrown = assertThrows(RuntimeException.class, () -> {
			this.tarefaService.createTask(new TarefaDTO(1l, "titulo", "desc", Prioridade.ALTA, 1), 2l, 1l);
		});
		
		verify(colunaRepository, times(1)).findById(2l);
		
		assertEquals("Column not found", thrown.getMessage());
	}
	
	@Test
	@DisplayName("Nao deve criar uma tarefa, quando a coluna nao pertence ao usuario")
	public void createTaskCase3() {
		Coluna coluna = new Coluna(1l, "titulo", 0, new Projeto(1l, "projeto", new Usuario(1l, "email@gmail.com", "123123123")));
		
		when(colunaRepository.findById(1l)).thenReturn(Optional.of(coluna));
		
		Exception thrown = assertThrows(RuntimeException.class, () -> {
			this.tarefaService.createTask(new TarefaDTO(1l, "titulo", "desc", Prioridade.ALTA, 1), 1l, 2l);
		});
		
		verify(colunaRepository, times(1)).findById(1l);
		
		assertEquals("The Task doesn't belong to the user", thrown.getMessage());
	}
	
	@Test
	@DisplayName("Deve atualizar uma tarefa com sucesso no BD")
	public void updateTaskCase1() {
		TarefaDTO data = new TarefaDTO(1l, "titulo", "desc", Prioridade.ALTA, 1);
		Coluna coluna = new Coluna(1l, "titulo", 0, new Projeto(1l, "projeto", new Usuario(1l, "email@gmail.com", "123123123")));
		
		when(tarefaRepository.findById(1l)).thenReturn(Optional.of(new Tarefa(1l, "titulo", "desc", Prioridade.ALTA, 1, coluna)));
		when(tarefaRepository.save(any(Tarefa.class))).thenReturn(new Tarefa(1l, "titulo2", "desc2", Prioridade.BAIXA, 1, coluna));
		
		TarefaDTO result = this.tarefaService.updateTask(1l, data, 1l);
		
		verify(tarefaRepository, times(1)).findById(1l);
		verify(tarefaRepository, times(1)).save(any(Tarefa.class));
		
		assertThat(result.id()).isEqualTo(1l);
		assertThat(result.titulo()).isEqualTo("titulo2");
		assertThat(result.descricao()).isEqualTo("desc2");
		assertThat(result.prioridade()).isEqualTo(Prioridade.BAIXA);
	}
	
	@Test
	@DisplayName("Nao deve atualizar uma tarefa, quando a tarefa nao existe")
	public void updateTaskCase2() {
		TarefaDTO data = new TarefaDTO(1l, "titulo", "desc", Prioridade.ALTA, 1);
		
		Exception thrown = assertThrows(RuntimeException.class, () -> {
			this.tarefaService.updateTask(1l, data, 1l);
		});
		
		verify(tarefaRepository, times(1)).findById(1l);
		
		assertEquals("Task not found", thrown.getMessage());
	}
	
	@Test
	@DisplayName("Nao deve atualizar uma tarefa, quando a tarefa nao pertence ao usuario")
	public void updateTaskCase3() {
		TarefaDTO data = new TarefaDTO(1l, "titulo", "desc", Prioridade.ALTA, 1);
		Coluna coluna = new Coluna(1l, "titulo", 0, new Projeto(1l, "projeto", new Usuario(1l, "email@gmail.com", "123123123")));
		
		when(tarefaRepository.findById(1l)).thenReturn(Optional.of(new Tarefa(1l, "titulo", "desc", Prioridade.ALTA, 1, coluna)));
		
		Exception thrown = assertThrows(RuntimeException.class, () -> {
			this.tarefaService.updateTask(1l, data, 2l);
		});
		
		verify(tarefaRepository, times(1)).findById(1l);
		
		assertEquals("The Task doesn't belong to the user", thrown.getMessage());
	}
	
	@Test
	@DisplayName("Deve deletar uma tarefa com sucesso do BD")
	public void deleteTaskCase1() {
		Coluna coluna = new Coluna(1l, "titulo", 0, new Projeto(1l, "projeto", new Usuario(1l, "email@gmail.com", "123123123")));
		Tarefa tarefa = new Tarefa(1l, "titulo", "desc", Prioridade.ALTA, 1, coluna);
		
		when(tarefaRepository.findById(1l)).thenReturn(Optional.of(tarefa));
		
		this.tarefaService.deleteTask(1l, 1l);
		
		verify(tarefaRepository, times(1)).findById(1l);
		verify(tarefaRepository, times(1)).delete(any(Tarefa.class));
		
	}
	
	@Test
	@DisplayName("Nao deve deletar uma tarefa, quando a tarefa nao existe")
	public void deleteTaskCase2() {
		Exception thrown = assertThrows(RuntimeException.class, () -> {
			this.tarefaService.deleteTask(1l, 1l);
		});
		
		verify(tarefaRepository, times(1)).findById(1l);
		
		assertEquals("Task not found", thrown.getMessage());
			
	}
	
	@Test
	@DisplayName("Nao deve deletar uma tarefa, quando a tarefa nao pertence ao usuario")
	public void deleteTaskCase3() {
		Coluna coluna = new Coluna(1l, "titulo", 0, new Projeto(1l, "projeto", new Usuario(1l, "email@gmail.com", "123123123")));
		Tarefa tarefa = new Tarefa(1l, "titulo", "desc", Prioridade.ALTA, 1, coluna);
		
		when(tarefaRepository.findById(1l)).thenReturn(Optional.of(tarefa));
		
		Exception thrown = assertThrows(RuntimeException.class, () -> {
			this.tarefaService.deleteTask(1l, 2l);
		});
		
		verify(tarefaRepository, times(1)).findById(1l);
		
		assertEquals("The Task doesn't belong to the user", thrown.getMessage());
	}
	
	@Test
	@DisplayName("Deve mover uma tarefa com sucesso no BD")
	public void moveTaskCase1() {
		Coluna coluna1 = new Coluna(1l, "titulo", 0, new Projeto(1l, "projeto", new Usuario(1l, "email@gmail.com", "123123123")));
		Coluna coluna2 = new Coluna(2l, "titulo2", 0, new Projeto(1l, "projeto", new Usuario(1l, "email@gmail.com", "123123123")));
		Tarefa tarefa = new Tarefa(1l, "titulo", "desc", Prioridade.ALTA, 1, coluna1);
		MoverTarefaDTO mover = new MoverTarefaDTO(2l, 0);
		
		when(tarefaRepository.findById(1l)).thenReturn(Optional.of(tarefa));
		when(colunaRepository.findById(2l)).thenReturn(Optional.of(coluna2));
		when(tarefaRepository.findByOrdem(mover.idNovaOrdem())).thenReturn(Optional.empty());
		when(tarefaRepository.save(any(Tarefa.class))).thenReturn(new Tarefa(1l, "titulo", "desc", Prioridade.ALTA, 1, coluna2));
		
		this.tarefaService.moveTask(1l, mover, 1l);
		
		verify(tarefaRepository, times(1)).findById(1l);
		verify(colunaRepository, times(1)).findById(2l);
		verify(tarefaRepository, times(1)).save(any(Tarefa.class));
	}
	
	@Test
	@DisplayName("Nao deve mover uma tarefa, quando a tarefa nao existe")
	public void moveTaskCase2() {
		MoverTarefaDTO mover = new MoverTarefaDTO(2l, 0);
		
		Exception thrown = assertThrows(RuntimeException.class, () -> {
			this.tarefaService.moveTask(1l, mover, 1l);
		});
		
		verify(tarefaRepository, times(1)).findById(1l);
		
		assertEquals("Task not found", thrown.getMessage());
	}
	
	@Test
	@DisplayName("Nao deve mover uma tarefa, quando a nova coluna nao existe")
	public void moveTaskCase3() {
		MoverTarefaDTO mover = new MoverTarefaDTO(2l, 0);
		Coluna coluna1 = new Coluna(1l, "titulo", 0, new Projeto(1l, "projeto", new Usuario(1l, "email@gmail.com", "123123123")));
		Tarefa tarefa = new Tarefa(1l, "titulo", "desc", Prioridade.ALTA, 1, coluna1);
		
		when(tarefaRepository.findById(1l)).thenReturn(Optional.of(tarefa));
		
		Exception thrown = assertThrows(RuntimeException.class, () -> {
			this.tarefaService.moveTask(1l, mover, 1l);
		});
		
		verify(tarefaRepository, times(1)).findById(1l);
		verify(colunaRepository, times(1)).findById(2l);
		
		assertEquals("Column not found", thrown.getMessage());
	}
	
	@Test
	@DisplayName("Nao deve mover uma tarefa, quando a tarefa nao pertence ao usuario")
	public void moveTaskCase4() {
		MoverTarefaDTO mover = new MoverTarefaDTO(2l, 0);
		Coluna coluna1 = new Coluna(1l, "titulo", 0, new Projeto(1l, "projeto", new Usuario(1l, "email@gmail.com", "123123123")));
		Coluna coluna2 = new Coluna(2l, "titulo2", 0, new Projeto(1l, "projeto", new Usuario(1l, "email@gmail.com", "123123123")));
		Tarefa tarefa = new Tarefa(1l, "titulo", "desc", Prioridade.ALTA, 1, coluna1);
		
		when(tarefaRepository.findById(1l)).thenReturn(Optional.of(tarefa));
		when(colunaRepository.findById(2l)).thenReturn(Optional.of(coluna2));
		
		Exception thrown = assertThrows(RuntimeException.class, () -> {
			this.tarefaService.moveTask(1l, mover, 2l);
		});
		
		verify(tarefaRepository, times(1)).findById(1l);
		verify(colunaRepository, times(1)).findById(2l);
		
		assertEquals("The Task doesn't belong to the user", thrown.getMessage());
	}
	
	@Test
	@DisplayName("Nao deve mover uma tarefa, quando a nova coluna nao pertence ao usuario")
	public void moveTaskCase5() {
		MoverTarefaDTO mover = new MoverTarefaDTO(2l, 0);
		Coluna coluna1 = new Coluna(1l, "titulo", 0, new Projeto(1l, "projeto", new Usuario(1l, "email@gmail.com", "123123123")));
		Coluna coluna2 = new Coluna(2l, "titulo2", 0, new Projeto(1l, "projeto", new Usuario(2l, "email@gmail.com", "123123123")));
		Tarefa tarefa = new Tarefa(1l, "titulo", "desc", Prioridade.ALTA, 1, coluna1);
		
		when(tarefaRepository.findById(1l)).thenReturn(Optional.of(tarefa));
		when(colunaRepository.findById(2l)).thenReturn(Optional.of(coluna2));
		
		Exception thrown = assertThrows(RuntimeException.class, () -> {
			this.tarefaService.moveTask(1l, mover, 1l);
		});
		
		verify(tarefaRepository, times(1)).findById(1l);
		verify(colunaRepository, times(1)).findById(2l);
		
		assertEquals("The Task doesn't belong to the user", thrown.getMessage());
	}
}
