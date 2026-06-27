package com.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.repository.UsuarioRepository;
import com.proyecto.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String cuenta,
            @RequestParam String clave,
            HttpSession session,
            Model model) {

        var usuarioOpt = usuarioRepository.findByCuentaAndClave(cuenta, clave);

        if (usuarioOpt.isEmpty()) {
            model.addAttribute("alert", Alert.sweetAlertError("Cuenta o clave incorrecta"));
            return "login";
        }

        var usuario = usuarioOpt.get();

        if (!usuario.getActivo()) {
            model.addAttribute("alert", Alert.sweetAlertError("Tu cuenta estÃ¡ desactivada"));
            return "login";
        }

        session.setAttribute("usuarioSesion", usuario);

        if (usuario.getTipo().getIdTipo() == 1) {
            return "redirect:/home";
        }

        return "redirect:/pedido/listado";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}