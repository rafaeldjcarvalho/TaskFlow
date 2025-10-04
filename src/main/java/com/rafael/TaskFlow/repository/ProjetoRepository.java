package com.rafael.TaskFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rafael.TaskFlow.entity.Projeto;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

}
