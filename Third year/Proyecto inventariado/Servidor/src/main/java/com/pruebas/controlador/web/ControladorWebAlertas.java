package com.pruebas.controlador.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pruebas.modelo.producto.RepositorioProductos;
import com.pruebas.modelo.usuario.UserServiceImpl;
import com.pruebas.modelo.usuario.Usuario;

@Controller
public class ControladorWebAlertas {

	@Autowired
	private final RepositorioProductos repositorioProductos;

	@Autowired
	private UserServiceImpl userRepository;

	ControladorWebAlertas(RepositorioProductos repositorio) {
		this.repositorioProductos = repositorio;
	}

	@GetMapping("/app/alerts")

	public String alertsMainPage(Model md) {
		Usuario currentLoggedUser = userRepository.getCurrentUser();
		if (currentLoggedUser != null) {
			md.addAttribute("productos", repositorioProductos.getAllTriggeredProducts(currentLoggedUser.getId()));
			md.addAttribute("Photo",
					"https://inventarispro.s3.amazonaws.com/" + currentLoggedUser.getPhoto().getUbicacionImagen()
							+ (!currentLoggedUser.isDefault() ? "/avatar.png" : "/avatardefault.png"));
			return "alertas";
		} else {
			return "redirect:/logout";
		}
	}

}
