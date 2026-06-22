package com.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto.model.Pizza;
import com.proyecto.service.PizzaService;
import com.proyecto.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("pizza")
@RequiredArgsConstructor
public class PizzaController {
	private final PizzaService pizzaService;
	
	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstPizzas", pizzaService.listar());
		return "pizza/listado";
	}
	
	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("pizza", new Pizza());
		return "pizza/nuevo";
	}
	
	@PostMapping("registrar")
	public String registrar(
			@ModelAttribute Pizza pizza,
			Model model,
			RedirectAttributes flash
			) {
		var response = pizzaService.create(pizza);
		
		if (!response.success()) {
			model.addAttribute("pizza", pizza);
			model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
			return "pizza/nuevo";
		}
		
		var toast = Alert.sweetToast(response.mensaje(), "success", 5000);
		flash.addFlashAttribute("toast", toast);
		return "redirect:/pizza/listado";
	}
	
	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model) {
		model.addAttribute("pizza", pizzaService.getOne(id));
		return "pizza/edicion";
	}
	
	@PostMapping("guardar")
	public String guardar(
			@ModelAttribute Pizza pizza,
			Model model,
			RedirectAttributes flash
			) {
		var response = pizzaService.update(pizza);
		
		if (!response.success()) {
			model.addAttribute("pizza", pizza);
			model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
			return "pizza/edicion";
		}
		
		var toast = Alert.sweetToast(response.mensaje(), "success", 5000);
		flash.addFlashAttribute("toast", toast);
		return "redirect:/pizza/listado";
	}

	@PostMapping("cambiar-estado/{id}")
	public String cambiarEstado(@PathVariable Integer id, RedirectAttributes flash) {
		var response = pizzaService.cambiarEstado(id);
		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000));
		return "redirect:/pizza/listado";
	}

	@PostMapping("eliminar/{id}")
	public String eliminar(@PathVariable Integer id, RedirectAttributes flash) {
		var response = pizzaService.eliminar(id);
		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000));
		return "redirect:/pizza/listado";
	}
	
}