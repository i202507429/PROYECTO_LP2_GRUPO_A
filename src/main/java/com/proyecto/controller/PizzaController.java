package com.proyecto.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto.model.Pizza;
import com.proyecto.repository.CategoriaPizzaRepository;
import com.proyecto.repository.PizzaTamanioRepository;
import com.proyecto.repository.TamanioRepository;
import com.proyecto.service.PizzaService;
import com.proyecto.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("pizza")
@RequiredArgsConstructor
public class PizzaController {

    private final PizzaService             pizzaService;
    private final TamanioRepository        tamanioRepository;
    private final CategoriaPizzaRepository categoriaPizzaRepository;
    private final PizzaTamanioRepository   pizzaTamanioRepository;

    // ── LISTADO CON FILTROS ───────────────────────────────────────────────────

    @GetMapping("listado")
    public String listado(
            @RequestParam(required = false) String  nombre,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) Integer idCategoria,
            Model model) {

        model.addAttribute("lstPizzas",      pizzaService.filtrar(nombre, activo, idCategoria));
        model.addAttribute("lstCategorias",  categoriaPizzaRepository.findAll());
        model.addAttribute("nombreFiltro",   nombre);
        model.addAttribute("activoFiltro",   activo != null ? activo.toString() : "");
        model.addAttribute("categoriaFiltro", idCategoria);
        return "pizza/listado";
    }

    // ── NUEVO ─────────────────────────────────────────────────────────────────

    @GetMapping("nuevo")
    public String nuevo(Model model) {
        model.addAttribute("pizza",         new Pizza());
        model.addAttribute("lstTamanios",   tamanioRepository.findAll());
        model.addAttribute("lstCategorias", categoriaPizzaRepository.findAll());
        model.addAttribute("tamaniosActuales", List.of()); // ninguno marcado aún (se marca todo en HTML)
        return "pizza/nuevo";
    }

    @PostMapping("registrar")
    public String registrar(
            @ModelAttribute Pizza pizza,
            @RequestParam(required = false) List<Integer> tamanioIds,
            @RequestParam(required = false) List<Double>  precios,
            Model model,
            RedirectAttributes flash) {

        var response = pizzaService.create(pizza, tamanioIds, precios);

        if (!response.success()) {
            model.addAttribute("pizza",         pizza);
            model.addAttribute("lstTamanios",   tamanioRepository.findAll());
            model.addAttribute("lstCategorias", categoriaPizzaRepository.findAll());
            model.addAttribute("tamaniosActuales", List.of());
            model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
            return "pizza/nuevo";
        }

        flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), "success", 5000));
        return "redirect:/pizza/listado";
    }

    // ── EDICIÓN ───────────────────────────────────────────────────────────────

    @GetMapping("edicion/{id}")
    public String edicion(@PathVariable Integer id, Model model) {
        model.addAttribute("pizza",           pizzaService.getOne(id));
        model.addAttribute("lstTamanios",     tamanioRepository.findAll());
        model.addAttribute("lstCategorias",   categoriaPizzaRepository.findAll());
        // Los PizzaTamanio actuales para pre-marcar checkboxes y pre-rellenar precios
        model.addAttribute("tamaniosActuales", pizzaTamanioRepository.findByPizzaIdPizza(id));
        return "pizza/edicion";
    }

    @PostMapping("guardar")
    public String guardar(
            @ModelAttribute Pizza pizza,
            @RequestParam(required = false) List<Integer> tamanioIds,
            @RequestParam(required = false) List<Double>  precios,
            Model model,
            RedirectAttributes flash) {

        var response = pizzaService.update(pizza, tamanioIds, precios);

        if (!response.success()) {
            model.addAttribute("pizza",         pizza);
            model.addAttribute("lstTamanios",   tamanioRepository.findAll());
            model.addAttribute("lstCategorias", categoriaPizzaRepository.findAll());
            model.addAttribute("tamaniosActuales",
                    pizzaTamanioRepository.findByPizzaIdPizza(pizza.getIdPizza()));
            model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
            return "pizza/edicion";
        }

        flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), "success", 5000));
        return "redirect:/pizza/listado";
    }

    // ── ACCIONES ─────────────────────────────────────────────────────────────

    @PostMapping("cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Integer id, RedirectAttributes flash) {
        var response = pizzaService.cambiarEstado(id);
        flash.addFlashAttribute("toast",
                Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000));
        return "redirect:/pizza/listado";
    }

    @PostMapping("eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes flash) {
        var response = pizzaService.eliminar(id);
        flash.addFlashAttribute("toast",
                Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000));
        return "redirect:/pizza/listado";
    }
}
