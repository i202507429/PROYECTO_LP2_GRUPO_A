package com.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto.model.Adicional;
import com.proyecto.service.AdicionalService;
import com.proyecto.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("adicional")
@RequiredArgsConstructor
public class AdicionalController {

	private final AdicionalService adicionalService;

	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstAdicionales", adicionalService.listar());
		return "adicional/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("adicional", new Adicional());
		return "adicional/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(
			@ModelAttribute Adicional adicional,
			Model model,
			RedirectAttributes flash) {
		var response = adicionalService.create(adicional);

		if (!response.success()) {
			model.addAttribute("adicional", adicional);
			model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
			return "adicional/nuevo";
		}

		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), "success", 5000));
		return "redirect:/adicional/listado";
	}

	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model) {
		model.addAttribute("adicional", adicionalService.getOne(id));
		return "adicional/edicion";
	}

	@PostMapping("guardar")
	public String guardar(
			@ModelAttribute Adicional adicional,
			Model model,
			RedirectAttributes flash) {
		var response = adicionalService.update(adicional);

		if (!response.success()) {
			model.addAttribute("adicional", adicional);
			model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
			return "adicional/edicion";
		}

		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), "success", 5000));
		return "redirect:/adicional/listado";
	}

	@PostMapping("cambiar-estado/{id}")
	public String cambiarEstado(@PathVariable Integer id, RedirectAttributes flash) {
		var response = adicionalService.cambiarEstado(id);
		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000));
		return "redirect:/adicional/listado";
	}

	@PostMapping("eliminar/{id}")
	public String eliminar(@PathVariable Integer id, RedirectAttributes flash) {
		var response = adicionalService.eliminar(id);
		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000));
		return "redirect:/adicional/listado";
	}

}