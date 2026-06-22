package com.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.model.DetallePizza;

@Repository
public interface DetallePizzaRepository extends JpaRepository<DetallePizza, Integer> {

	List<DetallePizza> findByPedido_IdPedido(Integer idPedido);

}