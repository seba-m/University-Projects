package com.pruebas.modelo.fotos;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Foto {
	@Id
	@GeneratedValue
	private Long id;
	private String titulo;
	private String nombreImagen;
	private String ubicacionImagen;
	private Long size;

	public Foto() {
	}

	public Foto(String titulo, String nombreImagen, String ubicacionImagen, Long size) {
		this.titulo = titulo;
		this.nombreImagen = nombreImagen;
		this.ubicacionImagen = ubicacionImagen;
		this.size = size;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getNombreImagen() {
		return nombreImagen;
	}

	public void setNombreImagen(String nombreImagen) {
		this.nombreImagen = nombreImagen;
	}

	public String getUbicacionImagen() {
		return ubicacionImagen;
	}

	public void setUbicacionImagen(String ubicacionImagen) {
		this.ubicacionImagen = ubicacionImagen;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.titulo, this.nombreImagen, this.ubicacionImagen, this.size);
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof Foto))
			return false;

		if (this.hashCode() == o.hashCode())
			return true;

		Foto foto = (Foto) o;
		return Objects.equals(this.id, foto.id) && Objects.equals(this.titulo, foto.titulo)
				&& Objects.equals(this.nombreImagen, foto.nombreImagen)
				&& Objects.equals(this.ubicacionImagen, foto.ubicacionImagen) && Objects.equals(this.size, foto.size);
	}

}
