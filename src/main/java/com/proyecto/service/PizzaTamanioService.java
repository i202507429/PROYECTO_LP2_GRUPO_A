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

	public List<PizzaTamanio> listarPorPizza(Integer idPizza) {
		return pizzaTamanioRepository.findByPizzaIdPizza(idPizza);
	}

	public ResultadoResponse create(PizzaTamanio pizzaTamanio) {
		boolean existe = pizzaTamanioRepository.existsByPizzaIdPizzaAndTamanioIdTamanio(
				pizzaTamanio.getPizza().getIdPizza(),
				pizzaTamanio.getTamanio().getIdTamanio());

		if (existe) {
			return new ResultadoResponse(false, "Ya existe un precio para esa combinación de Pizza + Tamaño");
		}

		try {
			var registro = pizzaTamanioRepository.save(pizzaTamanio);
			return new ResultadoResponse(true, String.format("Registro con ID %s creado", registro.getIdPizzaTamanio()));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	public PizzaTamanio getOne(Integer id) {
		return pizzaTamanioRepository.findById(id).orElseThrow();
	}

	public ResultadoResponse update(PizzaTamanio pizzaTamanio) {
		boolean existe = pizzaTamanioRepository.existsByPizzaIdPizzaAndTamanioIdTamanioAndIdPizzaTamanioNot(
				pizzaTamanio.getPizza().getIdPizza(),
				pizzaTamanio.getTamanio().getIdTamanio(),
				pizzaTamanio.getIdPizzaTamanio());

		if (existe) {
			return new ResultadoResponse(false, "Ya existe un precio para esa combinación de Pizza + Tamaño");
		}

		try {
			var registro = pizzaTamanioRepository.save(pizzaTamanio);
			return new ResultadoResponse(true, String.format("Registro con ID %s actualizado", registro.getIdPizzaTamanio()));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}
}