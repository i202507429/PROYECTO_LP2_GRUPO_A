package com.proyecto.model;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_pizza")
@Getter
@Setter
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
public class Pizza {

	@Id
	@Column(name = "id_pizza")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idPizza;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "imagen")
	private String imagen;
	
	@Column(name = "activo")
	private Boolean activo;
	
}
