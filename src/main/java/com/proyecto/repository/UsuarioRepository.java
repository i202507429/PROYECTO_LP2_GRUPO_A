package com.proyecto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	Optional<Usuario> findByCuentaAndClave(String cuenta, String clave);

	boolean existsByCuenta(String cuenta);

	boolean existsByCuentaAndIdUsuarioNot(String cuenta, Integer idUsuario);

}