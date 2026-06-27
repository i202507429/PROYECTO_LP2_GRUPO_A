package com.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.model.PizzaTamanio;

@Repository
public interface PizzaTamanioRepository extends JpaRepository<PizzaTamanio, Integer> {

    // Buscar todos los tamaños de una pizza (para edición)
    List<PizzaTamanio> findByPizzaIdPizza(Integer idPizza);

    // Buscar todas las combinaciones de un tamaño (para eliminación en cascada)
    List<PizzaTamanio> findByTamanioIdTamanio(Integer idTamanio);

    // Validar duplicado al crear
    boolean existsByPizzaIdPizzaAndTamanioIdTamanio(Integer idPizza, Integer idTamanio);

    // Validar duplicado al editar
    boolean existsByPizzaIdPizzaAndTamanioIdTamanioAndIdPizzaTamanioNot(
            Integer idPizza, Integer idTamanio, Integer idPizzaTamanio);
}