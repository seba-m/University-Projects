package com.seba.inventariado.model;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public class Producto implements Serializable {
    private UUID id;
    private UUID ownerID;
    private String nombre;
    private String precio;
    private String cantidad;
    private Date creationDate;
    private Date updateDate;

    private String minLvl;
    private String minLvlOption = "0";
    private boolean minLvlEnabled;
    private boolean minLvlTriggered;
    private Date minLvlAlertDate;

    private String descripcion;

    private Set<Tag> tags = new HashSet<>();

    private Set<Foto> fotos;

    private HashMap<String, Long> fileNames;

    public Producto() {
        // esta vacio dado a que se le asignan datos usando getters y setters.
    }

    public boolean isMinLvlTriggered() {
        return minLvlTriggered;
    }

    public HashMap<String, Long> getFileNames() {
        return fileNames;
    }

    public void setFileNames(HashMap<String, Long> fileNames) {
        this.fileNames = fileNames;
    }

    public Date getMinLvlAlertDate() {
        return minLvlAlertDate;
    }

    public void setMinLvlAlertDate(Date minLvlAlertDate) {
        this.minLvlAlertDate = minLvlAlertDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public boolean getMinLvlTriggered() {
        return minLvlTriggered;
    }

    public void setMinLvlTriggered(boolean minLvlTriggered) {
        this.minLvlTriggered = minLvlTriggered;
    }

    public void checkAlarm() {
        switch (getMinLvlOption()) {
            case "1":
                setMinLvlTriggered(Long.parseLong(getCantidad()) < Long.parseLong(getMinLvl()));
                break;

            case "2":
                setMinLvlTriggered(Long.parseLong(getCantidad()) <= Long.parseLong(getMinLvl()));
                break;

            case "3":
                setMinLvlTriggered(Long.parseLong(getCantidad()) > Long.parseLong(getMinLvl()));
                break;

            case "4":
                setMinLvlTriggered(Long.parseLong(getCantidad()) >= Long.parseLong(getMinLvl()));
                break;
            default:
                setMinLvlTriggered(false);
                break;
        }

    }

    public String getMinLvlOption() {
        return minLvlOption;
    }

    public void setMinLvlOption(String minLvlOption) {
        this.minLvlOption = minLvlOption;
    }

    public boolean isMinLvlEnabled() {
        return minLvlEnabled;
    }

    public void setMinLvlEnabled(boolean minLvlEnabled) {
        this.minLvlEnabled = minLvlEnabled;
        if (!minLvlEnabled) {
            setMinLvlTriggered(false);
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {

        if (!StringUtils.isBlank(descripcion)) {
            this.descripcion = descripcion;
        }
    }

    public String getMinLvl() {
        return minLvl;
    }

    public void setMinLvl(String minLvl) {
        if (isValidNumber(minLvl) && isMinLvlOptionValid(getMinLvlOption()) && isMinLvlEnabled()) {
            // primer caso = tiene un valor valido, ha seleccionado una opcion valida, y
            // tiene activada la alarma

            this.minLvl = minLvl;
            setMinLvlAlertDate(new Date());
            checkAlarm();

        } else if (!isMinLvlEnabled()) {
            // segundo caso, tiene desactivada la alarma

            this.minLvl = null;
            setMinLvlAlertDate(null);
            setMinLvlOption("0");

        } else if ((!isValidNumber(getMinLvl()) || !isMinLvlOptionValid(getMinLvlOption())) && isMinLvlEnabled()) {
            // tercer caso, valor actual u opcion elegida es invalida y alarma esta activada

            setMinLvlEnabled(false);

        } else {
            checkAlarm();
        }
    }

    private boolean isMinLvlOptionValid(String option) {
        if (StringUtils.isBlank(option))
            return false;

        return Pattern.compile("^[0-4]{1,1}$").matcher(option).matches();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (!StringUtils.isBlank(nombre)) {
            this.nombre = nombre;
        }
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        if (isValidNumber(precio)) {
            this.precio = precio;
        }
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        if (isValidNumber(cantidad)) {
            this.cantidad = cantidad;
        }
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(UUID ownerID) {
        this.ownerID = ownerID;
    }

    public Set<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(Set<Foto> fotos) {
        this.fotos = fotos;
    }

    boolean containsTag(Tag tag) {
        if (getTags() == null) {
            return false;
        }
        return getTags().contains(tag);
    }

    private boolean isValidNumber(String option) {
        if (StringUtils.isBlank(option))
            return false;

        return Pattern.compile("^[0-9]{1,10}$").matcher(option).matches();
    }

    private boolean isValidText(String option) {
        return !StringUtils.isBlank(option);
    }

    public HashMap<String, Long> getAllFotosName() {
        HashMap<String, Long> paths = new HashMap<>();

        if (fotos != null && !fotos.isEmpty()) {
            for (Foto foto : fotos) {
                paths.put(foto.getNombreImagen(), foto.getSize());
            }
        }

        return paths;
    }

    public void removeFoto(String nombreFoto) {
        for (Foto foto : fotos) {
            if (foto.getNombreImagen().equals(nombreFoto)) {
                fotos.remove(foto);
                return;
            }
        }
    }

    public boolean containsString(String text) {
        if (StringUtils.isBlank(text))
            return false;

        if (isValidText(getNombre()) && getNombre().toLowerCase().contains(text))
            return true;

        if (isValidText(getPrecio()) && getPrecio().toLowerCase().contains(text))
            return true;

        if (isValidText(getCantidad()) && getCantidad().toLowerCase().contains(text))
            return true;

        if (getTags() != null && !getTags().isEmpty())
            for (Tag tag : getTags()) {
                if (tag.getNombre().toLowerCase().contains(text))
                    return true;
            }

        return false;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Producto))
            return false;
        Producto producto = (Producto) o;
        return Objects.equals(this.id, producto.id) && Objects.equals(this.nombre, producto.nombre)
                && Objects.equals(this.tags, producto.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.nombre, this.tags);
    }

    @NotNull
    @Override
    public String toString() {
        return "Producto [id=" + id + ", ownerID=" + ownerID + ", nombre=" + nombre + ", precio=" + precio
                + ", cantidad=" + cantidad + ", creationDate=" + creationDate + ", updateDate=" + updateDate
                + ", minLvl=" + minLvl + ", minLvlOption=" + minLvlOption + ", minLvlEnabled=" + minLvlEnabled
                + ", minLvlTriggered=" + minLvlTriggered + ", minLvlAlertDate=" + minLvlAlertDate + ", descripcion="
                + descripcion + ", tags=" + tags + ", fotos=" + fotos + "]";
    }

}
