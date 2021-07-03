package com.seba.inventariado.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.UUID;

public class Tag implements Serializable {
    private UUID id;
    private String nombre;

    public Tag() {
    }

    public Tag(String nombre) {
        this.nombre = nombre;
    }

    public Tag(String nombre, UUID id) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @NotNull
    @Override
    public String toString() {
        return getNombre();
    }
}