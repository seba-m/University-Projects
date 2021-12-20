package com.pruebas.modelo.producto;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import com.pruebas.modelo.fotos.Foto;
import com.pruebas.modelo.tags.Tag;

@Entity
public class Producto {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "VARCHAR(255)")
	@Type(type = "uuid-char")
	private UUID id;
	@Column(name = "ownerid", columnDefinition = "BINARY(16)")
	private UUID ownerID;
	@Column(name = "nombre", columnDefinition = "VARCHAR(40)")
	private String nombre;
	@Column(name = "precio", columnDefinition = "VARCHAR(9)")
	private String precio;
	@Column(name = "cantidad", columnDefinition = "VARCHAR(9)")
	private String cantidad;
	@CreationTimestamp
	private Date creationDate;
	@UpdateTimestamp
	private Date updateDate;

	@Column(name = "minLvl", columnDefinition = "VARCHAR(9)")
	private String minLvl;
	@Column(name = "minLvlOption", columnDefinition = "VARCHAR(1)")
	private String minLvlOption = "0";
	private boolean minLvlEnabled;
	private boolean minLvlTriggered;
	private Date minLvlAlertDate;

	@Column(name = "descripcion", columnDefinition = "Text", length = 4000)
	@Type(type = "text")
	private String descripcion;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Tag> tags = new HashSet<>();

	@ElementCollection
	private Set<Foto> fotos;

	public Producto() {
		// esta vacio dado a que se le asignan datos usando getters y setters.
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

		return Pattern.compile("^[0-9]{1,9}$").matcher(option).matches();
	}

	public Map<String, Long> getAllFotosName() {
		Map<String, Long> paths = new HashMap<>();

		if (fotos != null && !fotos.isEmpty()) {
			for (Foto foto : fotos) {
				paths.put(foto.getNombreImagen(), foto.getSize());
			}
		} else {
			paths = Collections.emptyMap();
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

	@Override
	public String toString() {
		return "Producto [id=" + id + ", ownerID=" + ownerID + ", nombre=" + nombre + ", precio=" + precio
				+ ", cantidad=" + cantidad + ", creationDate=" + creationDate + ", updateDate=" + updateDate
				+ ", minLvl=" + minLvl + ", minLvlOption=" + minLvlOption + ", minLvlEnabled=" + minLvlEnabled
				+ ", minLvlTriggered=" + minLvlTriggered + ", minLvlAlertDate=" + minLvlAlertDate + ", descripcion="
				+ descripcion + ", tags=" + tags + ", fotos=" + fotos + "]";
	}

}
