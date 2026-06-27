package com.proyecto.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.proyecto.dto.ResultadoResponse;
import com.proyecto.model.Tamanio;
import com.proyecto.repository.PizzaTamanioRepository;
import com.proyecto.repository.TamanioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TamanioService {

    private final TamanioRepository tamanioRepository;
    private final PizzaTamanioRepository pizzaTamanioRepository;

    public List<Tamanio> listar() {
        return tamanioRepository.findAll();
    }

    public ResultadoResponse create(Tamanio tamanio) {
        if (tamanioRepository.existsByDescripcion(tamanio.getDescripcion())) {
            return new ResultadoResponse(false, "Ya existe un tamaño con esa descripción");
        }
        try {
            var registro = tamanioRepository.save(tamanio);
            var mensaje = String.format("Tamaño con ID %s registrado", registro.getIdTamanio());
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }

    public Tamanio getOne(Integer idTamanio) {
        return tamanioRepository.findById(idTamanio).orElseThrow();
    }

    public ResultadoResponse update(Tamanio tamanio) {
        if (tamanioRepository.existsByDescripcionAndIdTamanioNot(tamanio.getDescripcion(), tamanio.getIdTamanio())) {
            return new ResultadoResponse(false, "Ya existe un tamaño con esa descripción");
        }
        try {
            var registro = tamanioRepository.save(tamanio);
            var mensaje = String.format("Tamaño con ID %s actualizado", registro.getIdTamanio());
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }

    public ResultadoResponse cambiarEstado(Integer id) {
        try {
            var tamanio = tamanioRepository.findById(id).orElseThrow();
            tamanio.setActivo(!tamanio.getActivo());
            tamanioRepository.save(tamanio);
            var mensaje = tamanio.getActivo() ? "Tamaño activado" : "Tamaño desactivado";
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }

    public ResultadoResponse eliminar(Integer id) {
        try {
            var relacionados = pizzaTamanioRepository.findByTamanioIdTamanio(id);
            if (!relacionados.isEmpty()) {
                pizzaTamanioRepository.deleteAll(relacionados);
            }
            tamanioRepository.deleteById(id);
            var mensaje = relacionados.isEmpty()
                    ? "Tamaño eliminado"
                    : String.format("Tamaño eliminado junto con %d combinación(es) de precio asociadas", relacionados.size());
            return new ResultadoResponse(true, mensaje);
        } catch (DataIntegrityViolationException e) {
            return new ResultadoResponse(false, "No se puede eliminar, el tamaño tiene pedidos relacionados");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }
}