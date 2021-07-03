package com.pruebas.modelo.producto;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.pruebas.modelo.tags.Tag;

@SuppressWarnings("serial")
public class ProductoDto implements Serializable {
	private String nombre;
	private String precio;
	private String cantidad;
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	@JsonProperty("tags")
	private HashSet<Tag> tags;
	private String minLvl;
	private String minLvlOption;
	private String minLvlEnabled;
	private String descripcion;
	private String fileNames;

	public ProductoDto() {
		// esta vacio dado a que se le asignan datos usando getters y setters.
	}

	public String getMinLvlOption() {
		return minLvlOption;
	}

	public void setMinLvlOption(String minLvlOption) {
		this.minLvlOption = minLvlOption;
	}

	public String getMinLvlEnabled() {
		if (minLvlEnabled != null && minLvlEnabled.lastIndexOf(",") != -1) {
			return minLvlEnabled.substring(0, minLvlEnabled.lastIndexOf(","));
		}
		return minLvlEnabled;
	}

	public void setMinLvlEnabled(String minLvlEnabled) {
		this.minLvlEnabled = minLvlEnabled;
	}

	public String getMinLvl() {
		return minLvl;
	}

	public void setMinLvl(String minLvl) {
		this.minLvl = minLvl;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public HashSet<Tag> getTags() {
		return tags;
	}

	public void setTags(HashSet<Tag> tags) {
		this.tags = tags;
	}

	public boolean isValidDto() {
		return (isValidName(nombre) && isValidNumber(precio) && isValidNumber(cantidad));
	}

	private boolean isValidName(String nombre) {
		if (StringUtils.isBlank(nombre))
			return false;

		// TODO: limitar la cantidad de caracteres del nombre del producto.
		return true;// nombre.length() < 10;
	}

	private boolean isValidNumber(String number) {
		if (StringUtils.isBlank(number))
			return false;

		return Pattern.compile("^[0-9]{1,9}$").matcher(number).matches();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Long> getFileNames() {
		Map<String, Long> fileNamesMap = Collections.emptyMap();

		if (StringUtils.isBlank(this.fileNames)) {
			return fileNamesMap;
		}

		try {
			fileNamesMap = new Gson().fromJson(this.fileNames, HashMap.class);
			// fileNamesMap = new ObjectMapper().readValue(this.fileNames, HashMap.class);
			return fileNamesMap;
		} catch (Exception failToParseString) {
			System.err.println("\nerror " + failToParseString.getMessage() + "\n"
					+ failToParseString.getCause().toString() + "\n");
			return fileNamesMap;
		}
	}

	public void setFileNames(String fileNames) {
		this.fileNames = fileNames;
	}

	@Override
	public String toString() {
		return "ProductoDto [nombre=" + nombre + ", precio=" + precio + ", cantidad=" + cantidad + ", tags=" + tags
				+ ", minLvl=" + minLvl + ", minLvlOption=" + minLvlOption + ", minLvlEnabled=" + minLvlEnabled
				+ ", descripcion=" + descripcion + ", fileNames=" + getFileNames() + "]";
	}

}
