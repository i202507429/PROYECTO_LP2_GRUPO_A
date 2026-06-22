package com.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.model.Tamanio;

@Repository
public interface TamanioRepository extends JpaRepository<Tamanio, Integer> {

	boolean existsByDescripcion(String descripcion);

	boolean existsByDescripcionAndIdTamanioNot(String descripcion, Integer idTamanio);

}