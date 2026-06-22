package com.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto.model.PizzaTamanio;
import com.proyecto.service.PizzaService;
import com.proyecto.service.PizzaTamanioService;
import com.proyecto.service.TamanioService;
import com.proyecto.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("pizza-tamanio")
@RequiredArgsConstructor
public class PizzaTamanioController {

	private final PizzaTamanioService pizzaTamanioService;
	private final PizzaService pizzaService;
	private final TamanioService tamanioService;

	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstPizzaTamanios", pizzaTamanioService.listar());
		return "pizza_tamanio/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("pizzaTamanio", new PizzaTamanio());
		model.addAttribute("lstPizzas", pizzaService.listar());
		model.addAttribute("lstTamanios", tamanioService.listar());
		return "pizza_tamanio/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(
			@ModelAttribute PizzaTamanio pizzaTamanio,
			Model model,
			RedirectAttributes flash) {
		var response = pizzaTamanioService.create(pizzaTamanio);

		if (!response.success()) {
			model.addAttribute("pizzaTamanio", pizzaTamanio);
			model.addAttribute("lstPizzas", pizzaService.listar());
			model.addAttribute("lstTamanios", tamanioService.listar());
			model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
			return "pizza_tamanio/nuevo";
		}

		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), "success", 5000));
		return "redirect:/pizza-tamanio/listado";
	}

	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model) {
		model.addAttribute("pizzaTamanio", pizzaTamanioService.getOne(id));
		model.addAttribute("lstPizzas", pizzaService.listar());
		model.addAttribute("lstTamanios", tamanioService.listar());
		return "pizza_tamanio/edicion";
	}

	@PostMapping("guardar")
	public String guardar(
			@ModelAttribute PizzaTamanio pizzaTamanio,
			Model model,
			RedirectAttributes flash) {
		var response = pizzaTamanioService.update(pizzaTamanio);

		if (!response.success()) {
			model.addAttribute("pizzaTamanio", pizzaTamanio);
			model.addAttribute("lstPizzas", pizzaService.listar());
			model.addAttribute("lstTamanios", tamanioService.listar());
			model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
			return "pizza_tamanio/edicion";
		}

		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), "success", 5000));
		return "redirect:/pizza-tamanio/listado";
	}

}
