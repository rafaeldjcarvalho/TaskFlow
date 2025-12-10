package com.rafael.TaskFlow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rafael.TaskFlow.entity.Tarefa;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
	
	@Query("SELECT t FROM Tarefa t JOIN t.coluna c WHERE c.id = :coluna_id")
	List<Tarefa> findTarefasByColuna(@Param("coluna_id") Long coluna_id);
	
	//buscar tarefa por ordem
	//buscar a quantidade de tarefas(total de tarefas - 1)
	@Query("SELECT t FROM Tarefa t WHERE t.ordem = :ordem")
	Optional<Tarefa> findByOrdem(@Param("ordem") int ordem);
}
