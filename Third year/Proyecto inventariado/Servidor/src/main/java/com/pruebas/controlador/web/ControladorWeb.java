package com.pruebas.controlador.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pruebas.modelo.producto.Producto;
import com.pruebas.modelo.producto.RepositorioProductos;
import com.pruebas.modelo.tags.RepositorioTags;
import com.pruebas.modelo.usuario.UserServiceImpl;
import com.pruebas.modelo.usuario.Usuario;

@Controller
public class ControladorWeb {

	@Autowired
	private final RepositorioProductos repositorio;

	@Autowired
	private final RepositorioTags repositorioTags;

	@Autowired
	private UserServiceImpl userRepository;

	ControladorWeb(RepositorioProductos repositorio, RepositorioTags repositorioTags) {
		this.repositorio = repositorio;
		this.repositorioTags = repositorioTags;
	}

	@GetMapping("/app")
	public String appContoller(Model model) {
		Usuario currentLoggedUser = userRepository.getCurrentUser();
		if (currentLoggedUser != null) {
			model.addAttribute("itemAmmount", repositorio.getAllProducts(currentLoggedUser.getId()).size());
			model.addAttribute("tagsAmmount", repositorioTags.findAllByOwnerID(currentLoggedUser.getId()).size());
			model.addAttribute("stockAmmount", repositorio.getStockAmmountByUser(currentLoggedUser.getId()));
			model.addAttribute("totalAmmount", repositorio.getTotalAmmountByUser(currentLoggedUser.getId()));
			model.addAttribute("lastLogin", currentLoggedUser.getLastLogin());
			model.addAttribute("Photo",
					"https://inventarispro.s3.amazonaws.com/" + currentLoggedUser.getPhoto().getUbicacionImagen()
							+ (!currentLoggedUser.isDefault() ? "/avatar.png" : "/avatardefault.png"));

			List<Producto> triggered = repositorio.getAllTriggeredProducts(currentLoggedUser.getId());

			String alertAmmount = null;

			if (triggered != null && !triggered.isEmpty()) {
				alertAmmount = (triggered.size() > 9) ? "9+" : String.valueOf(triggered.size());
			}

			model.addAttribute("AlertAmmount", alertAmmount);

			return "app";
		} else {
			return "redirect:/logout";
		}
	}

	@GetMapping("/app/inventory")
	public String inventarioRoot(Model md) {
		Usuario currentLoggedUser = userRepository.getCurrentUser();
		if (currentLoggedUser != null) {
			md.addAttribute("productos", repositorio.getAllProducts(currentLoggedUser.getId()));
			md.addAttribute("Photo",
					"https://inventarispro.s3.amazonaws.com/" + currentLoggedUser.getPhoto().getUbicacionImagen()
							+ (!currentLoggedUser.isDefault() ? "/avatar.png" : "/avatardefault.png"));

			List<Producto> triggered = repositorio.getAllTriggeredProducts(currentLoggedUser.getId());

			String alertAmmount = null;

			if (triggered != null && !triggered.isEmpty()) {
				alertAmmount = (triggered.size() > 9) ? "9+" : String.valueOf(triggered.size());
			}

			md.addAttribute("AlertAmmount", alertAmmount);

			return "inventariado";
		} else {
			return "redirect:/logout";
		}
	}

}
