package com.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto.model.Usuario;
import com.proyecto.service.TipoService;
import com.proyecto.service.UsuarioService;
import com.proyecto.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("usuario")
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService usuarioService;
	private final TipoService tipoService;

	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstUsuarios", usuarioService.listar());
		return "usuario/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("lstTipos", tipoService.listar());
		return "usuario/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(
			@ModelAttribute Usuario usuario,
			Model model,
			RedirectAttributes flash) {
		var response = usuarioService.create(usuario);

		if (!response.success()) {
			model.addAttribute("usuario", usuario);
			model.addAttribute("lstTipos", tipoService.listar());
			model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
			return "usuario/nuevo";
		}

		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), "success", 5000));
		return "redirect:/usuario/listado";
	}

	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model) {
		model.addAttribute("usuario", usuarioService.getOne(id));
		model.addAttribute("lstTipos", tipoService.listar());
		return "usuario/edicion";
	}

	@PostMapping("guardar")
	public String guardar(
			@ModelAttribute Usuario usuario,
			Model model,
			RedirectAttributes flash) {
		var response = usuarioService.update(usuario);

		if (!response.success()) {
			model.addAttribute("usuario", usuario);
			model.addAttribute("lstTipos", tipoService.listar());
			model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
			return "usuario/edicion";
		}

		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), "success", 5000));
		return "redirect:/usuario/listado";
	}

	@PostMapping("cambiar-estado/{id}")
	public String cambiarEstado(@PathVariable Integer id, RedirectAttributes flash) {
		var response = usuarioService.cambiarEstado(id);
		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000));
		return "redirect:/usuario/listado";
	}

	@PostMapping("eliminar/{id}")
	public String eliminar(@PathVariable Integer id, RedirectAttributes flash) {
		var response = usuarioService.eliminar(id);
		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000));
		return "redirect:/usuario/listado";
	}

}