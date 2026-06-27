package com.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.model.Tipo;
import com.proyecto.repository.TipoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoService {

	private final TipoRepository tipoRepository;

	public List<Tipo> listar() {
		return tipoRepository.findAll();
	}

	public Tipo getOne(Integer id) {
		return tipoRepository.findById(id).orElseThrow();
	}

}
