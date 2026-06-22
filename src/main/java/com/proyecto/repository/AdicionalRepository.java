package com.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.model.Adicional;

@Repository
public interface AdicionalRepository extends JpaRepository<Adicional, Integer> {

	List<Adicional> findByActivoTrue();

	boolean existsByDescripcion(String descripcion);

	boolean existsByDescripcionAndIdAdicionalNot(String descripcion, Integer idAdicional);

}