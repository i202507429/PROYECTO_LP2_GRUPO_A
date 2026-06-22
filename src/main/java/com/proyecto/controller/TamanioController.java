package com.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto.model.Tamanio;
import com.proyecto.service.TamanioService;
import com.proyecto.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("tamanio")
@RequiredArgsConstructor
public class TamanioController {

	private final TamanioService tamanioService;

	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstTamanios", tamanioService.listar());
		return "tamanio/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("tamanio", new Tamanio());
		return "tamanio/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(
			@ModelAttribute Tamanio tamanio,
			Model model,
			RedirectAttributes flash) {
		var response = tamanioService.create(tamanio);

		if (!response.success()) {
			model.addAttribute("tamanio", tamanio);
			model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
			return "tamanio/nuevo";
		}

		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), "success", 5000));
		return "redirect:/tamanio/listado";
	}

	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model) {
		model.addAttribute("tamanio", tamanioService.getOne(id));
		return "tamanio/edicion";
	}

	@PostMapping("guardar")
	public String guardar(
			@ModelAttribute Tamanio tamanio,
			Model model,
			RedirectAttributes flash) {
		var response = tamanioService.update(tamanio);

		if (!response.success()) {
			model.addAttribute("tamanio", tamanio);
			model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
			return "tamanio/edicion";
		}

		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), "success", 5000));
		return "redirect:/tamanio/listado";
	}

	@PostMapping("cambiar-estado/{id}")
	public String cambiarEstado(@PathVariable Integer id, RedirectAttributes flash) {
		var response = tamanioService.cambiarEstado(id);
		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000));
		return "redirect:/tamanio/listado";
	}

	@PostMapping("eliminar/{id}")
	public String eliminar(@PathVariable Integer id, RedirectAttributes flash) {
		var response = tamanioService.eliminar(id);
		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000));
		return "redirect:/tamanio/listado";
	}

}