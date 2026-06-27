package com.proyecto.controller;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.proyecto.dto.ResultadoResponse;
import com.proyecto.repository.CategoriaPizzaRepository;
import com.proyecto.service.AdicionalService;
import com.proyecto.service.PedidoService;
import com.proyecto.service.PizzaService;
import com.proyecto.service.PizzaTamanioService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("catalogo")
@RequiredArgsConstructor
public class CatalogoController {

    private final PizzaService pizzaService;
    private final PizzaTamanioService pizzaTamanioService;
    private final AdicionalService adicionalService;
    private final PedidoService pedidoService;
    private final CategoriaPizzaRepository categoriaPizzaRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("lstPizzas", pizzaService.listarActivas());
        model.addAttribute("lstPizzaTamanios", pizzaTamanioService.listar());
        model.addAttribute("lstAdicionales", adicionalService.listarActivos());
        model.addAttribute("lstCategorias", categoriaPizzaRepository.findAll());
        return "catalogo/index";
    }

    @PostMapping("realizar-pedido")
    @ResponseBody
    public ResultadoResponse realizarPedido(
            @RequestParam String nombreCliente,
            @RequestParam String metodoPago,
            @RequestParam(required = false) List<Integer> idsPizzaTamanio,
            @RequestParam(required = false) List<Integer> cantidadesPizza,
            @RequestParam(required = false) List<Integer> idsAdicional,
            @RequestParam(required = false) List<Integer> cantidadesAdicional) {
        return pedidoService.registrarPedidoCliente(
                nombreCliente, metodoPago, idsPizzaTamanio, cantidadesPizza, idsAdicional, cantidadesAdicional);
    }
}