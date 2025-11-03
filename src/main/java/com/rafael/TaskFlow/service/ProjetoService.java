package com.rafael.TaskFlow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rafael.TaskFlow.entity.Coluna;
import com.rafael.TaskFlow.entity.Projeto;
import com.rafael.TaskFlow.entity.Usuario;
import com.rafael.TaskFlow.entity.dtos.ColunaDTO;
import com.rafael.TaskFlow.entity.dtos.ProjetoRequest;
import com.rafael.TaskFlow.entity.dtos.ProjetoResponse;
import com.rafael.TaskFlow.entity.dtos.TarefaDTO;
import com.rafael.TaskFlow.entity.dtos.mapper.TarefaMapper;
import com.rafael.TaskFlow.repository.ColunaRepository;
import com.rafael.TaskFlow.repository.ProjetoRepository;
import com.rafael.TaskFlow.repository.TarefaRepository;
import com.rafael.TaskFlow.repository.UsuarioRepository;

@Service
public class ProjetoService {
	
	@Autowired
	private ProjetoRepository projetoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ColunaRepository colunaRepository;
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	@Autowired
	private TarefaMapper tarefaMapper;
	
	public List<Projeto> getAllProjects(Long usuario_id) {
		return projetoRepository.findProjetosByUsuario(usuario_id);
	}
	
	public ProjetoResponse getProjectById(Long projeto_id, Long usuario_id) {
		Projeto projeto = projetoRepository.findById(projeto_id).orElseThrow(() -> new RuntimeException("Project not found"));
		Usuario usuario = usuarioRepository.findById(usuario_id).orElseThrow(() -> new RuntimeException("User not found"));
		
		List<ColunaDTO> colunasDTO = new ArrayList<ColunaDTO>();
		List<Coluna> colunasDoProjeto = colunaRepository.findColunasByProjeto(projeto_id);
		
		for(Coluna coluna : colunasDoProjeto) {
			List<TarefaDTO> tarefas = tarefaRepository.findTarefasByColuna(coluna.getId())
					.stream()
					.map(tarefaMapper::toDTO)
					.collect(Collectors.toList());
			ColunaDTO novaColuna = new ColunaDTO(coluna.getId(), coluna.getTitulo(), coluna.getOrdem(), tarefas);
			colunasDTO.add(novaColuna);
		}
		
		return new ProjetoResponse(projeto.getId(), projeto.getNome(), usuario.getId(), colunasDTO);
	}
	
	public Projeto createNewProject(ProjetoRequest data) {
		Usuario usuario = usuarioRepository.findById(data.usuario_id()).orElseThrow(() -> new RuntimeException("User not found"));
		
		Projeto novoProjeto = new Projeto();
		novoProjeto.setNome(data.nome());
		novoProjeto.setUsuario(usuario);
		
		Projeto projetoSalvo = this.saveProject(novoProjeto);
		
		this.createColumn("A Fazer", 0, projetoSalvo);
		this.createColumn("Em Andamento", 1, projetoSalvo);
		this.createColumn("Concluido", 2, projetoSalvo);
		
		return projetoSalvo;
	}
	
	private Projeto saveProject(Projeto data) {
		return projetoRepository.save(data);
	}
	
	private void createColumn(String titulo, int ordem, Projeto projeto) {
		Coluna coluna = new Coluna();
		coluna.setTitulo(titulo);
		coluna.setOrdem(ordem);
		coluna.setProjeto(projeto);
		this.saveColumn(coluna);
	}
	
	private Coluna saveColumn(Coluna data) {
		return colunaRepository.save(data);
	}

}
