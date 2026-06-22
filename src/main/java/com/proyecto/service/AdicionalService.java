package com.proyecto.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.proyecto.dto.ResultadoResponse;
import com.proyecto.model.Adicional;
import com.proyecto.repository.AdicionalRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AdicionalService {

	private final AdicionalRepository adicionalRepository;

	public List<Adicional> listar() {
		return adicionalRepository.findAll();
	}

	public List<Adicional> listarActivos() {
		return adicionalRepository.findByActivoTrue();
	}

	public ResultadoResponse create(Adicional adicional) {

		if (adicionalRepository.existsByDescripcion(adicional.getDescripcion())) {
			return new ResultadoResponse(false, "Ya existe un adicional con esa descripción");
		}

		try {
			var registro = adicionalRepository.save(adicional);
			var mensaje = String.format("Adicional con ID %s registrado", registro.getIdAdicional());
			return new ResultadoResponse(true, mensaje);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en el transacción");
		}
	}

	public Adicional getOne(Integer idAdicional) {
		return adicionalRepository.findById(idAdicional).orElseThrow();
	}

	public ResultadoResponse update(Adicional adicional) {

		if (adicionalRepository.existsByDescripcionAndIdAdicionalNot(adicional.getDescripcion(),
				adicional.getIdAdicional())) {
			return new ResultadoResponse(false, "Ya existe un adicional con esa descripción");
		}

		try {
			var registro = adicionalRepository.save(adicional);
			var mensaje = String.format("Adicional con ID %s actualizado", registro.getIdAdicional());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}

	}

	public ResultadoResponse cambiarEstado(Integer id) {
		try {
			var adicional = adicionalRepository.findById(id).orElseThrow();
			adicional.setActivo(!adicional.getActivo());
			adicionalRepository.save(adicional);
			var mensaje = adicional.getActivo() ? "Adicional activado" : "Adicional desactivado";
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	public ResultadoResponse eliminar(Integer id) {
		try {
			adicionalRepository.deleteById(id);
			return new ResultadoResponse(true, "Adicional eliminado");
		} catch (DataIntegrityViolationException e) {
			return new ResultadoResponse(false, "No se puede eliminar, el adicional tiene pedidos relacionados");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

}