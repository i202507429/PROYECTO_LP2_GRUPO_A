package com.proyecto.model;

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
@Table(name = "tbl_detalle_pizza")
@Getter
@Setter
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
public class DetallePizza {

	@Id
	@Column(name = "id_detalle")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idDetalle;
	
	@Column(name = "cantidad")
	private Integer cantidad;
	
	@Column(name = "subtotal")
	private Double subtotal;
	
	@ManyToOne
	@JoinColumn(name = "id_pedido")
	private Pedido pedido;
	
	@ManyToOne
	@JoinColumn(name = "id_pizza_tamanio")
	private PizzaTamanio pizzaTamanio;
	
}
