package com.pruebas.modelo.fotos;

public enum FotoPath {
	USER("user-photos/%s"), PRODUCT("/product-photos/%s/%s"), PRODUCT_FILE("product-photos/%s/%s/%s");

	private String path;

	private FotoPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
