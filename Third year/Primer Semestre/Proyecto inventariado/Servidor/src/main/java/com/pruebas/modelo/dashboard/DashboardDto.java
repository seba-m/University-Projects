package com.pruebas.modelo.dashboard;

import java.util.List;

import com.pruebas.modelo.producto.Producto;

public class DashboardDto {
	String items = "0";
	String tags = "0";
	String totalQuantity = "0";
	String totalValue = "0";
	String lastLogin = "";
	List<Producto> recentEditProducts;

	public DashboardDto() {
	}

	public DashboardDto(String items, String tags, String totalQuantity, String totalValue, String lastLogin,
			List<Producto> recentEditProducts) {
		super();
		this.items = items;
		this.tags = tags;
		this.totalQuantity = totalQuantity;
		this.totalValue = totalValue;
		this.lastLogin = lastLogin;
		this.recentEditProducts = recentEditProducts;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(String totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public String getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public List<Producto> getRecentEditProducts() {
		return recentEditProducts;
	}

	public void setRecentEditProducts(List<Producto> recentEditProducts) {
		this.recentEditProducts = recentEditProducts;
	}

}
