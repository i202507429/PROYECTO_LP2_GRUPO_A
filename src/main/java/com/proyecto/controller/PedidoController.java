package com.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto.service.PedidoService;
import com.proyecto.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("pedido")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping("listado")
    public String listado(Model model) {
        model.addAttribute("lstPedidos", pedidoService.listar());
        return "pedido/listado";
    }

    @GetMapping("comprobante/{id}")
    public String comprobante(@PathVariable Integer id, Model model) {
        model.addAttribute("idPedido", id);
        return "pedido/comprobante";
    }
    
    @GetMapping("detalle/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        model.addAttribute("pedido", pedidoService.obtenerPorId(id));
        // Si necesitas los detalles de pizza y adicionales:
        model.addAttribute("lstDetallePizza", pedidoService.listarDetallePizza(id));
        model.addAttribute("lstDetalleAdicional", pedidoService.listarDetalleAdicional(id));
        return "pedido/detalle";
    }

    @PostMapping("cambiar-estado/{id}")
    public String cambiarEstado(
            @PathVariable Integer id,
            @RequestParam String estado,
            RedirectAttributes flash) {
        var response = pedidoService.updateEstado(id, estado);
        flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000));
        return "redirect:/pedido/listado";
    }
}