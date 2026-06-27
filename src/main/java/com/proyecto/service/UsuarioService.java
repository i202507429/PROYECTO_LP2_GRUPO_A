package com.proyecto.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.proyecto.dto.ResultadoResponse;
import com.proyecto.model.Usuario;
import com.proyecto.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public List<Usuario> listar() {
		return usuarioRepository.findAll();
	}

	public ResultadoResponse create(Usuario usuario) {

		if (usuarioRepository.existsByCuenta(usuario.getCuenta())) {
			return new ResultadoResponse(false, "Ya existe un usuario con esa cuenta");
		}

		try {
			var registro = usuarioRepository.save(usuario);
			var mensaje = String.format("Usuario con ID %s registrado", registro.getIdUsuario());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	public Usuario getOne(Integer id) {
		return usuarioRepository.findById(id).orElseThrow();
	}

	public ResultadoResponse update(Usuario usuario) {

		if (usuarioRepository.existsByCuentaAndIdUsuarioNot(usuario.getCuenta(), usuario.getIdUsuario())) {
			return new ResultadoResponse(false, "Ya existe un usuario con esa cuenta");
		}

		try {
			var registro = usuarioRepository.save(usuario);
			var mensaje = String.format("Usuario con ID %s actualizado", registro.getIdUsuario());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	public ResultadoResponse cambiarEstado(Integer id) {
		try {
			var usuario = usuarioRepository.findById(id).orElseThrow();
			usuario.setActivo(!usuario.getActivo());
			usuarioRepository.save(usuario);
			var mensaje = usuario.getActivo() ? "Usuario activado" : "Usuario desactivado";
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	public ResultadoResponse eliminar(Integer id) {
		try {
			usuarioRepository.deleteById(id);
			return new ResultadoResponse(true, "Usuario eliminado");
		} catch (DataIntegrityViolationException e) {
			return new ResultadoResponse(false, "No se puede eliminar, el usuario tiene pedidos relacionados");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

}