package com.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.model.Pizza;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Integer> {

	List<Pizza> findByActivoTrue();

	boolean existsByNombre(String nombre);

	boolean existsByNombreAndIdPizzaNot(String nombre, Integer idPizza);

}