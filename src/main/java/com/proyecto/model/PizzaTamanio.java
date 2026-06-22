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
@Table(name = "tbl_pizza_tamanio")
@Getter
@Setter
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
public class PizzaTamanio {

	@Id
	@Column(name = "id_pizza_tamanio")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idPizzaTamanio;
	
	@Column(name = "precio")
	private Double precio;
	
	@ManyToOne
	@JoinColumn(name = "id_pizza")
	private Pizza pizza;
	
	@ManyToOne
	@JoinColumn(name = "id_tamanio")
	private Tamanio tamanio;
}
