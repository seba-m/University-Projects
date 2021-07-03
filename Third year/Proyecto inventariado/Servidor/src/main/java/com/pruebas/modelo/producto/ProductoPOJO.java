package com.pruebas.modelo.producto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pruebas.modelo.tags.Tag;

public class ProductoPOJO {
	private List<Producto> productos;

	public ProductoPOJO(List<Producto> productos) {
		this.productos = productos;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public List<Producto> productsWithTag(Tag tag) {
		if (productos == null || productos.isEmpty()) {
			return Collections.emptyList();
		}

		List<Producto> productosConTag = new ArrayList<>();

		for (Producto producto : productos) {
			if (producto.containsTag(tag)) {
				productosConTag.add(producto);
			}
		}

		return productosConTag;
	}
}
