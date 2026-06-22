package com.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.dto.ResultadoResponse;
import com.proyecto.model.PizzaTamanio;
import com.proyecto.repository.PizzaTamanioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PizzaTamanioService {

	private final PizzaTamanioRepository pizzaTamanioRepository;

	
	public List<PizzaTamanio> listar() {
		return pizzaTamanioRepository.findAll();
	}

	public ResultadoResponse create(PizzaTamanio pizzaTamanio) {
		try {
			var registro = pizzaTamanioRepository.save(pizzaTamanio);
			var mensaje = String.format("Registro con ID %s creado", registro.getIdPizzaTamanio());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	public PizzaTamanio getOne(Integer id) {
		return pizzaTamanioRepository.findById(id).orElseThrow();
	}

	public ResultadoResponse update(PizzaTamanio pizzaTamanio) {
		try {
			var registro = pizzaTamanioRepository.save(pizzaTamanio);
			var mensaje = String.format("Registro con ID %s actualizado", registro.getIdPizzaTamanio());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

}