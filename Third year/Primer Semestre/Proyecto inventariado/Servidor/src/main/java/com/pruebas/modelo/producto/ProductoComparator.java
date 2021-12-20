package com.pruebas.modelo.producto;

import java.util.Comparator;

public class ProductoComparator implements Comparator<Producto> {

	@Override
	public int compare(Producto o1, Producto o2) {
		return Integer.valueOf(o1.getCantidad()) - Integer.valueOf(o2.getCantidad());
	}

}
