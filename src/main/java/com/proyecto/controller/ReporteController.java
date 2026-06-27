package com.proyecto.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.service.ReporteService;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping("reporte")
public class ReporteController {

	@Autowired
	private ReporteService reporteService;

	@GetMapping("pedido")
	public void pedido(@RequestParam Integer numPedido, HttpServletResponse response) throws Exception {

	    String reportPath = "/reporte/pedido.jrxml";

	    Map<String, Object> params = new HashMap<>();
	    params.put("pNumPedido", numPedido);
	    params.put("pLogo", getClass().getResource("/reporte/imagenes/logo.png").toString());

	    JasperPrint jasperPrint = reporteService.getJasperPrint(params, reportPath);

	    response.setContentType("application/pdf");
	    response.setHeader("Content-Disposition", String.format("inline; filename=pedido-nro-%s.pdf", numPedido));

	    OutputStream outputStream = response.getOutputStream();
	    JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
	    outputStream.flush();
	    outputStream.close();
	}
}