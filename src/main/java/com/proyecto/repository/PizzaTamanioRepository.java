package com.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.model.PizzaTamanio;

@Repository
public interface PizzaTamanioRepository extends JpaRepository<PizzaTamanio, Integer> {

}
