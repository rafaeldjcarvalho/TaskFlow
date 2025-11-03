package com.rafael.TaskFlow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rafael.TaskFlow.entity.Projeto;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
	
	@Query("SELECT p FROM Projeto p JOIN p.usuario u WHERE u.id = :usuario_id")
	List<Projeto> findProjetosByUsuario(@Param("usuario_id") Long usuario_id);

}
