package com.rafael.TaskFlow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rafael.TaskFlow.entity.Coluna;

@Repository
public interface ColunaRepository extends JpaRepository<Coluna, Long> {

	@Query("SELECT c FROM Coluna c JOIN c.projeto p WHERE p.id = :projeto_id")
	List<Coluna> findColunasByProjeto(@Param("projeto_id") Long projeto_id);
}
