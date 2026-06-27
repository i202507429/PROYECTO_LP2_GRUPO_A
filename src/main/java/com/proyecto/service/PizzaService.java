package com.proyecto.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.dto.ResultadoResponse;
import com.proyecto.model.Pizza;
import com.proyecto.model.PizzaTamanio;
import com.proyecto.model.Tamanio;
import com.proyecto.repository.PizzaRepository;
import com.proyecto.repository.PizzaTamanioRepository;
import com.proyecto.repository.TamanioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PizzaService {

    private final PizzaRepository       pizzaRepository;
    private final PizzaTamanioRepository pizzaTamanioRepository;
    private final TamanioRepository     tamanioRepository;

    public List<Pizza> listar() {
        return pizzaRepository.findAll();
    }

    public List<Pizza> listarActivas() {
        return pizzaRepository.findByActivoTrue();
    }

    public List<Pizza> filtrar(String nombre, Boolean activo, Integer idCategoria) {
        return pizzaRepository.filtrar(nombre, activo, idCategoria);
    }

    /**
     * Crea la pizza y sus PizzaTamanio asociados en una sola transacción.
     * precios: lista paralela a tamanioIds (precio por cada tamaño seleccionado)
     */
    @Transactional
    public ResultadoResponse create(Pizza pizza, List<Integer> tamanioIds, List<Double> precios) {

        if (pizzaRepository.existsByNombre(pizza.getNombre())) {
            return new ResultadoResponse(false, "Ya existe una pizza con ese nombre");
        }

        try {
            Pizza guardada = pizzaRepository.save(pizza);

            if (tamanioIds != null) {
                for (int i = 0; i < tamanioIds.size(); i++) {
                    Integer idTamanio = tamanioIds.get(i);
                    Double  precio    = (precios != null && i < precios.size()) ? precios.get(i) : 0.0;

                    Tamanio tamanio = tamanioRepository.findById(idTamanio).orElseThrow();
                    PizzaTamanio pt = new PizzaTamanio();
                    pt.setPizza(guardada);
                    pt.setTamanio(tamanio);
                    pt.setPrecio(precio);
                    pizzaTamanioRepository.save(pt);
                }
            }

            return new ResultadoResponse(true,
                    String.format("Pizza con ID %s registrada", guardada.getIdPizza()));

        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }

    public Pizza getOne(Integer idPizza) {
        return pizzaRepository.findById(idPizza).orElseThrow();
    }

    /**
     * Actualiza pizza y sincroniza sus PizzaTamanio:
     * - Si el tamaño ya existía → actualiza precio
     * - Si es nuevo → crea
     * - Si fue desmarcado → elimina
     */
    @Transactional
    public ResultadoResponse update(Pizza pizza, List<Integer> tamanioIds, List<Double> precios) {

        if (pizzaRepository.existsByNombreAndIdPizzaNot(pizza.getNombre(), pizza.getIdPizza())) {
            return new ResultadoResponse(false, "Ya existe una pizza con ese nombre");
        }

        try {
            pizzaRepository.save(pizza);

            // Eliminar los que ya no están seleccionados
            List<PizzaTamanio> actuales = pizzaTamanioRepository.findByPizzaIdPizza(pizza.getIdPizza());
            for (PizzaTamanio pt : actuales) {
                if (tamanioIds == null || !tamanioIds.contains(pt.getTamanio().getIdTamanio())) {
                    pizzaTamanioRepository.delete(pt);
                }
            }

            // Crear o actualizar los seleccionados
            if (tamanioIds != null) {
                for (int i = 0; i < tamanioIds.size(); i++) {
                    Integer idTamanio = tamanioIds.get(i);
                    Double  precio    = (precios != null && i < precios.size()) ? precios.get(i) : 0.0;

                    PizzaTamanio pt = actuales.stream()
                            .filter(p -> p.getTamanio().getIdTamanio().equals(idTamanio))
                            .findFirst()
                            .orElse(null);

                    if (pt == null) {
                        pt = new PizzaTamanio();
                        pt.setPizza(pizza);
                        Tamanio tamanio = tamanioRepository.findById(idTamanio).orElseThrow();
                        pt.setTamanio(tamanio);
                    }
                    pt.setPrecio(precio);
                    pizzaTamanioRepository.save(pt);
                }
            }

            return new ResultadoResponse(true,
                    String.format("Pizza con ID %s actualizada", pizza.getIdPizza()));

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
            return new ResultadoResponse(false,
                    "No se puede eliminar, la pizza tiene pedidos relacionados");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }
}
