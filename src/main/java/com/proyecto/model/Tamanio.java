package com.proyecto.model;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_tamanio")
@Getter
@Setter
@DynamicInsert
public class Tamanio {

	@Id
	@Column(name = "id_tamanio")
	private Integer idTamanio;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "activo")
	private Boolean activo;
	
}