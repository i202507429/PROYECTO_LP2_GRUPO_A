package com.proyecto.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.dto.ResultadoResponse;
import com.proyecto.model.DetalleAdicional;
import com.proyecto.model.DetallePizza;
import com.proyecto.model.Pedido;
import com.proyecto.repository.AdicionalRepository;
import com.proyecto.repository.DetalleAdicionalRepository;
import com.proyecto.repository.DetallePizzaRepository;
import com.proyecto.repository.PedidoRepository;
import com.proyecto.repository.PizzaTamanioRepository;
import com.proyecto.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PizzaTamanioRepository pizzaTamanioRepository;
    private final AdicionalRepository adicionalRepository;
    private final DetallePizzaRepository detallePizzaRepository;
    private final DetalleAdicionalRepository detalleAdicionalRepository;

    public List<Pedido> listar() {
        return pedidoRepository.findAllByOrderByIdPedidoDesc();
    }

    public Pedido getOne(Integer id) {
        return pedidoRepository.findById(id).orElseThrow();
    }

    public ResultadoResponse updateEstado(Integer id, String estado) {
        try {
            var pedido = pedidoRepository.findById(id).orElseThrow();
            pedido.setEstado(estado);
            pedidoRepository.save(pedido);
            return new ResultadoResponse(true, "Estado actualizado a: " + estado);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }

    @Transactional
    public ResultadoResponse registrarPedidoCliente(
            String nombreCliente,
            String metodoPago,
            List<Integer> idsPizzaTamanio,
            List<Integer> cantidadesPizza,
            List<Integer> idsAdicional,
            List<Integer> cantidadesAdicional) {

        try {
            var pedido = new Pedido();
            pedido.setFecha(LocalDateTime.now());
            pedido.setEstado("pendiente");
            pedido.setNombreCliente(nombreCliente);
            pedido.setMetodoPago(metodoPago);
            pedido.setUsuario(usuarioRepository.findById(1).orElseThrow());
            pedido.setTotal(0.0);
            var pedidoGuardado = pedidoRepository.save(pedido);

            double total = 0.0;

            if (idsPizzaTamanio != null) {
                for (int i = 0; i < idsPizzaTamanio.size(); i++) {
                    var pt = pizzaTamanioRepository.findById(idsPizzaTamanio.get(i)).orElseThrow();
                    var cantidad = cantidadesPizza.get(i);
                    var subtotal = pt.getPrecio() * cantidad;
                    total += subtotal;

                    var detalle = new DetallePizza();
                    detalle.setPedido(pedidoGuardado);
                    detalle.setPizzaTamanio(pt);
                    detalle.setCantidad(cantidad);
                    detalle.setSubtotal(subtotal);
                    detallePizzaRepository.save(detalle);
                }
            }

            if (idsAdicional != null) {
                for (int i = 0; i < idsAdicional.size(); i++) {
                    var adicional = adicionalRepository.findById(idsAdicional.get(i)).orElseThrow();
                    var cantidad = cantidadesAdicional.get(i);
                    var subtotal = adicional.getPrecio() * cantidad;
                    total += subtotal;

                    var detalle = new DetalleAdicional();
                    detalle.setPedido(pedidoGuardado);
                    detalle.setAdicional(adicional);
                    detalle.setCantidad(cantidad);
                    detalle.setSubtotal(subtotal);
                    detalleAdicionalRepository.save(detalle);
                }
            }

            pedidoGuardado.setTotal(total);
            pedidoRepository.save(pedidoGuardado);

            return new ResultadoResponse(true, "Pedido #" + pedidoGuardado.getIdPedido() + " registrado", pedidoGuardado.getIdPedido());

        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error al registrar el pedido");
        }
    }

    public Pedido obtenerPorId(Integer id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public List<DetallePizza> listarDetallePizza(Integer idPedido) {
        return detallePizzaRepository.findByPedido_IdPedido(idPedido);
    }

    public List<DetalleAdicional> listarDetalleAdicional(Integer idPedido) {
        return detalleAdicionalRepository.findByPedido_IdPedido(idPedido);
    }
}