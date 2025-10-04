package com.rafael.TaskFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rafael.TaskFlow.entity.Coluna;

@Repository
public interface ColunaRepository extends JpaRepository<Coluna, Long> {

}
