package com.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.model.DetalleAdicional;

@Repository
public interface DetalleAdicionalRepository extends JpaRepository<DetalleAdicional, Integer> {

	List<DetalleAdicional> findByPedido_IdPedido(Integer idPedido);

}