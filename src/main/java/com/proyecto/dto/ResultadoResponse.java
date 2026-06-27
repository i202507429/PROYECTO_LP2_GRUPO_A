package com.proyecto.dto;

public record ResultadoResponse(boolean success, String mensaje, Integer idPedido) {

	public ResultadoResponse(boolean success, String mensaje) {
		this(success, mensaje, null);
	}
}