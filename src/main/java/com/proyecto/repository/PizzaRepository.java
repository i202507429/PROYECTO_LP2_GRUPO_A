package com.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto.model.Pizza;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Integer> {

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdPizzaNot(String nombre, Integer idPizza);

    List<Pizza> findByActivoTrue();

    // Filtro combinado: nombre parcial, estado y/o categoría
    @Query("SELECT p FROM Pizza p " +
           "WHERE (:nombre IS NULL OR :nombre = '' OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
           "AND (:activo IS NULL OR p.activo = :activo) " +
           "AND (:idCategoria IS NULL OR p.categoria.idCategoria = :idCategoria)")
    List<Pizza> filtrar(@Param("nombre") String nombre,
                        @Param("activo") Boolean activo,
                        @Param("idCategoria") Integer idCategoria);
}
