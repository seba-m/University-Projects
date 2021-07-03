package com.seba.inventariado.model;

import java.io.Serializable;

public class Foto implements Serializable {
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

}