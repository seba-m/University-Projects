package com.pruebas.controlador.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.pruebas.modelo.producto.Producto;
import com.pruebas.modelo.producto.RepositorioProductos;
import com.pruebas.modelo.usuario.UserServiceImpl;
import com.pruebas.modelo.usuario.UserUpdatingDto;
import com.pruebas.modelo.usuario.Usuario;

@Controller
@RequestMapping("/app/account")
public class ControladorWebCuenta {

	@Autowired
	private UserServiceImpl userRepository;

	private final RepositorioProductos repositorio;

	ControladorWebCuenta(RepositorioProductos repositorio) {
		this.repositorio = repositorio;
	}

	@ModelAttribute("UsuarioActualizado")
	public UserUpdatingDto UserUpdatingDto() {
		return new UserUpdatingDto();
	}

	@GetMapping
	public String controladorCuenta(Model model) {
		try {
			Usuario currentLoggedUser = userRepository.getCurrentUser();
			model.addAttribute("Email", !currentLoggedUser.isEmptyEmail() ? currentLoggedUser.getEmail() : "No Email");
			model.addAttribute("FirstName",
					!currentLoggedUser.isEmptyFirstName() ? currentLoggedUser.getFirstName() : "No First Name");
			model.addAttribute("LastName",
					!currentLoggedUser.isEmptyLastName() ? currentLoggedUser.getLastName() : "No Last Name");
			model.addAttribute("Photo",
					"https://inventarispro.s3.amazonaws.com/" + currentLoggedUser.getPhoto().getUbicacionImagen()
							+ (!currentLoggedUser.isDefault() ? "/avatar.png" : "/avatardefault.png"));

			List<Producto> triggered = repositorio.getAllTriggeredProducts(currentLoggedUser.getId());

			String alertAmmount = null;

			if (triggered != null && !triggered.isEmpty()) {
				alertAmmount = (triggered.size() > 9) ? "9+" : String.valueOf(triggered.size());
			}

			model.addAttribute("AlertAmmount", alertAmmount);

			return "account";
		} catch (Exception e) {
			return "redirect:/logout";
		}
	}

	@PostMapping
	public String controladorCuentaActualizada(@ModelAttribute("UsuarioActualizado") UserUpdatingDto registrationDto,
			@RequestParam("image") MultipartFile multipartFile) {
		return userRepository.updateUserData(registrationDto, multipartFile);
	}

	@GetMapping("/deleteAccount")
	public String eliminarCuenta() {
		if (userRepository.deleteAccount()) {
			return "redirect:/login?delete_success";
		} else {
			return "redirect:/app/account?fail_delete";
		}

	}
}
