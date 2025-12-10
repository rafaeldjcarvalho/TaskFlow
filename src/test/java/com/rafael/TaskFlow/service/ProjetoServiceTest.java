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
import com.rafael.TaskFlow.entity.Usuario;
import com.rafael.TaskFlow.entity.dtos.ProjetoRequest;
import com.rafael.TaskFlow.entity.dtos.ProjetoResponse;
import com.rafael.TaskFlow.entity.dtos.mapper.ProjetoMapper;
import com.rafael.TaskFlow.entity.dtos.mapper.TarefaMapper;
import com.rafael.TaskFlow.repository.ColunaRepository;
import com.rafael.TaskFlow.repository.ProjetoRepository;
import com.rafael.TaskFlow.repository.TarefaRepository;
import com.rafael.TaskFlow.repository.UsuarioRepository;

public class ProjetoServiceTest {
	
	@Mock
	private ProjetoRepository projetoRepository;
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@Mock
	private ColunaRepository colunaRepository;
	
	@Mock
	private TarefaRepository tarefaRepository;
	
	@Mock
	private ProjetoMapper projetoMapper;
	
	@Mock
	private TarefaMapper tarefaMapper;

	@Autowired
	@InjectMocks
	private ProjetoService projetoService;
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	@DisplayName("Deve receber um projeto com sucesso do BD")
	public void getProjectByIdCase1() {
		Usuario usuario = new Usuario(1l,"email@gmail.com", "123123123");
		Projeto projeto = new Projeto(1l, "titulo", usuario);
		
		when(projetoRepository.findById(1l)).thenReturn(Optional.of(projeto));
		when(usuarioRepository.findById(1l)).thenReturn(Optional.of(usuario));
		when(colunaRepository.findColunasByProjeto(1l)).thenReturn(new ArrayList<Coluna>());
		
		ProjetoResponse result = projetoService.getProjectById(1l, 1l);
		
		verify(projetoRepository, times(1)).findById(1l);
		verify(usuarioRepository, times(1)).findById(1l);
		verify(colunaRepository, times(1)).findColunasByProjeto(1l);
		
		assertThat(result.id()).isEqualTo(projeto.getId());
		assertThat(result.nome()).isEqualTo(projeto.getNome());
		assertThat(result.usuario_id()).isEqualTo(usuario.getId());
	}
	
	@Test
	@DisplayName("Nao deve receber um projeto, quando o projeto nao existe")
	public void getProjectByIdCase2() {
		Exception thrown = assertThrows(RuntimeException.class, () -> {
			projetoService.getProjectById(1l, 1l);
		});
		
		verify(projetoRepository, times(1)).findById(1l);
		
		assertEquals("Project not found", thrown.getMessage());
	}
	
	@Test
	@DisplayName("Nao deve receber um projeto, quando o usuario nao existe")
	public void getProjectByIdCase3() {
		Projeto projeto = new Projeto(1l, "titulo", new Usuario());
		
		when(projetoRepository.findById(1l)).thenReturn(Optional.of(projeto));
		
		Exception thrown = assertThrows(RuntimeException.class, () -> {
			projetoService.getProjectById(1l, 1l);
		});
		
		verify(projetoRepository, times(1)).findById(1l);
		verify(usuarioRepository, times(1)).findById(1l);
		
		assertEquals("User not found", thrown.getMessage());
	}
	
	@Test
	@DisplayName("Nao deve receber um projeto, quando o projeto nao pertence ao usuario")
	public void getProjectByIdCase4() {
		Usuario usuario = new Usuario(1l,"email@gmail.com", "123123123");
		Projeto projeto = new Projeto(1l, "titulo", usuario);
		
		when(projetoRepository.findById(1l)).thenReturn(Optional.of(projeto));
		when(usuarioRepository.findById(2l)).thenReturn(Optional.of(new Usuario(2l, "outro@gmail.com", "1231231234")));
		
		Exception thrown = assertThrows(RuntimeException.class, () -> {
			projetoService.getProjectById(1l, 2l);
		});
		
		verify(projetoRepository, times(1)).findById(1l);
		verify(usuarioRepository, times(1)).findById(2l);
		
		assertEquals("The task doesn't belong to the user", thrown.getMessage());
	}
	
	@Test
	@DisplayName("Deve criar um projeto com sucesso")
	public void createNewProjectCase1() {
		ProjetoRequest data = new ProjetoRequest("titulo");
		Usuario usuario = new Usuario(1l, "email@gmail.com", "321321312");
		Projeto projeto = new Projeto(1l, "titulo", usuario);
		
		when(usuarioRepository.findById(1l)).thenReturn(Optional.of(usuario));
		when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);
		when(colunaRepository.save(any(Coluna.class))).thenReturn(new Coluna());
		when(projetoMapper.toDTO(any(Projeto.class))).thenReturn(new ProjetoResponse(projeto.getId(), projeto.getNome(), usuario.getId(), null));
		
		ProjetoResponse result = this.projetoService.createNewProject(data, usuario.getId());
		
		verify(usuarioRepository, times(1)).findById(1l);
		verify(projetoRepository, times(1)).save(any(Projeto.class));
		verify(colunaRepository, times(3)).save(any(Coluna.class));
		verify(projetoMapper, times(1)).toDTO(any(Projeto.class));
		
		assertThat(result.id()).isEqualTo(projeto.getId());
		assertThat(result.nome()).isEqualTo(projeto.getNome());
		assertThat(result.usuario_id()).isEqualTo(usuario.getId());
		assertThat(result.colunas()).isNull();
	}
}
