package com.pruebas.controlador.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pruebas.modelo.dashboard.DashboardDto;
import com.pruebas.modelo.producto.RepositorioProductos;
import com.pruebas.modelo.tags.RepositorioTags;
import com.pruebas.modelo.usuario.UserServiceImpl;
import com.pruebas.modelo.usuario.Usuario;

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("isAuthenticated()")
public class ControladorApp {

	@Autowired
	private final RepositorioProductos repositorio;

	@Autowired
	private final RepositorioTags repositorioTags;

	@Autowired
	private UserServiceImpl userRepository;

	ControladorApp(RepositorioProductos repositorio, RepositorioTags repositorioTags) {
		this.repositorio = repositorio;
		this.repositorioTags = repositorioTags;
	}

	@GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public DashboardDto dashboard() {
		Usuario currentLoggedUser = userRepository.getCurrentUser();

		DashboardDto resumen = new DashboardDto();

		resumen.setItems(String.valueOf(repositorio.getAllProducts(currentLoggedUser.getId()).size()));
		resumen.setTags(String.valueOf(repositorioTags.findAllByOwnerID(currentLoggedUser.getId()).size()));
		resumen.setTotalQuantity(String.valueOf(repositorio.getStockAmmountByUser(currentLoggedUser.getId())));
		resumen.setTotalValue(String.valueOf(repositorio.getTotalAmmountByUser(currentLoggedUser.getId())));
		resumen.setLastLogin(String.valueOf(currentLoggedUser.getLastLogin()));
		resumen.setRecentEditProducts(repositorio.getRecentEditProducts(currentLoggedUser.getId()));

		return resumen;

	}
}
