package com.proyecto.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_pedido")
@Getter
@Setter
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

	@Id
	@Column(name = "id_pedido")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idPedido;
	
	@Column(name = "fecha")
	private LocalDateTime fecha;
	
	@Column(name = "estado")
	private String estado;
	
	@Column(name = "total")
	private Double total;
	
	@Column(name = "nombre_cliente")
	private String nombreCliente;
	
	@Column(name = "metodo_pago")
	private String metodoPago;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	
}