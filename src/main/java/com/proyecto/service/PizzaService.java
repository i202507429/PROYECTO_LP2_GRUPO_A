package com.proyecto.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.proyecto.dto.ResultadoResponse;
import com.proyecto.model.Pizza;
import com.proyecto.repository.PizzaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PizzaService {

	private final PizzaRepository pizzaRepository;

	public List<Pizza> listar() {
		return pizzaRepository.findAll();
	}

	
	public List<Pizza> listarActivas() {
		return pizzaRepository.findByActivoTrue();
	}

	public ResultadoResponse create(Pizza pizza) {

		if (pizzaRepository.existsByNombre(pizza.getNombre())) {
			return new ResultadoResponse(false, "Ya existe una pizza con ese nombre");
		}

		try {
			var registro = pizzaRepository.save(pizza);
			var mensaje = String.format("Pizza con ID %s registrada", registro.getIdPizza());
			return new ResultadoResponse(true, mensaje);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en el transacción");
		}
	}

	public Pizza getOne(Integer idPizza) {
		return pizzaRepository.findById(idPizza).orElseThrow();
	}

	public ResultadoResponse update(Pizza pizza) {

		if (pizzaRepository.existsByNombreAndIdPizzaNot(pizza.getNombre(), pizza.getIdPizza())) {
			return new ResultadoResponse(false, "Ya existe una pizza con ese nombre");
		}

		try {
			var registro = pizzaRepository.save(pizza);
			var mensaje = String.format("Pizza con ID %s actualizada", registro.getIdPizza());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}

	}

	public ResultadoResponse cambiarEstado(Integer id) {
		try {
			var pizza = pizzaRepository.findById(id).orElseThrow();
			pizza.setActivo(!pizza.getActivo());
			pizzaRepository.save(pizza);
			var mensaje = pizza.getActivo() ? "Pizza activada" : "Pizza desactivada";
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	public ResultadoResponse eliminar(Integer id) {
		try {
			pizzaRepository.deleteById(id);
			return new ResultadoResponse(true, "Pizza eliminada");
		} catch (DataIntegrityViolationException e) {
			return new ResultadoResponse(false, "No se puede eliminar, la pizza tiene tamaños o pedidos relacionados");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}
}