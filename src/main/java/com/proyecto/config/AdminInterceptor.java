package com.proyecto.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import com.proyecto.model.Usuario;

public class AdminInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("usuarioSesion");

		if (usuario == null) {
			response.sendRedirect("/");
			return false;
		}

		if (usuario.getTipo().getIdTipo() != 1) {
			response.sendRedirect("/pedido/listado");
			return false;
		}

		return true;
	}
}