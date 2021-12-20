package com.pruebas.controlador.web;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pruebas.modelo.producto.Producto;
import com.pruebas.modelo.producto.ProductoPOJO;
import com.pruebas.modelo.producto.RepositorioProductos;
import com.pruebas.modelo.tags.RepositorioTags;
import com.pruebas.modelo.tags.Tag;
import com.pruebas.modelo.usuario.UserServiceImpl;
import com.pruebas.modelo.usuario.Usuario;

@Controller
public class ControladorWebTags {

	@Autowired
	private final RepositorioTags repositorioTags;

	@Autowired
	private final RepositorioProductos repositorioProductos;

	@Autowired
	private UserServiceImpl userRepository;

	ControladorWebTags(RepositorioProductos repositorio, RepositorioTags repositorioTags) {
		this.repositorioProductos = repositorio;
		this.repositorioTags = repositorioTags;
	}

	@GetMapping("/app/inventory/tags")
	public String tagMainPage(Model model) {
		Usuario currentLoggedUser = userRepository.getCurrentUser();
		model.addAttribute("AllTags", repositorioTags.findAllByOwnerID(currentLoggedUser.getId()));

		ProductoPOJO productos = new ProductoPOJO(repositorioProductos.getAllProducts(currentLoggedUser.getId()));
		model.addAttribute("AllProducts", productos);

		List<Producto> triggered = repositorioProductos.getAllTriggeredProducts(currentLoggedUser.getId());

		String alertAmmount = null;

		if (triggered != null && !triggered.isEmpty()) {
			alertAmmount = (triggered.size() > 9) ? "9+" : String.valueOf(triggered.size());
		}

		model.addAttribute("AlertAmmount", alertAmmount);

		model.addAttribute("Photo",
				"https://inventarispro.s3.amazonaws.com/" + currentLoggedUser.getPhoto().getUbicacionImagen()
						+ (!currentLoggedUser.isDefault() ? "/avatar.png" : "/avatardefault.png"));

		return "tags";
	}

	@PostMapping("/app/inventory/tags")
	public String tagAdd(@RequestParam("tag") String tagName) {
		try {

			if (StringUtils.isBlank(tagName))
				return "redirect:/app/inventory/tags?fail=invalidName";

			Usuario currentLoggedUser = userRepository.getCurrentUser();

			if (repositorioTags.findByNombre(currentLoggedUser.getId(), tagName).isPresent())
				return "redirect:/app/inventory/tags?fail=alreadyExists";

			Tag newTag = new Tag(tagName, currentLoggedUser.getId());

			repositorioTags.save(newTag);

			return "redirect:/app/inventory/tags?success";
		} catch (Exception e) {
			return "redirect:/app/inventory/tags?fail";
		}

	}

	/**
	 * Esta función se encarga de cambiar el nombre del tag.
	 * 
	 * @param tagName es el nombre nuevo del tag
	 * @param id      es el id del tag
	 */
	@PostMapping("/app/inventory/tags/edit/{id}")
	public String tagEditPost(@RequestParam("tag") String tagName, @PathVariable UUID id) {
		try {

			if (StringUtils.isBlank(tagName))
				return "redirect:/app/inventory/tags?fail=invalidName";

			if (!isOwnerTag(id)) {
				return "redirect:/app/inventory/tags?fail=ParamNull";
			}

			Usuario currentLoggedUser = userRepository.getCurrentUser();

			if (repositorioTags.findByNombre(currentLoggedUser.getId(), tagName).isPresent())
				return "redirect:/app/inventory/tags?fail=alreadyExists";

			Tag newTag = repositorioTags.findById(id).get();

			newTag.setNombre(tagName);

			repositorioTags.save(newTag);

			return "redirect:/app/inventory/tags?success";
		} catch (Exception e) {
			return "redirect:/app/inventory/tags?fail";
		}

	}

	@GetMapping("/app/inventory/tags/delete/{id}")
	public String tagDelete(@PathVariable UUID id) {
		if (!isOwnerTag(id)) {
			return "redirect:/app/inventory/tags?fail=ParamNull";
		}

		Tag tagAEliminar = repositorioTags.findById(id).get();

		if (tagAEliminar != null) {

			Usuario currentLoggedUser = userRepository.getCurrentUser();
			repositorioProductos.removeTagFromProducts(currentLoggedUser.getId(), tagAEliminar);
			repositorioTags.deleteById(id);

			return "redirect:/app/inventory/tags?success=Delete";
		} else {
			return "redirect:/app/inventory/tags?fail=NoItemFound";
		}
	}

	private boolean isOwnerTag(UUID tagId) {
		if (tagId == null) {
			return false;
		}
		try {
			Usuario currentLoggedUser = userRepository.getCurrentUser();
			Optional<Tag> tagEdit = repositorioTags.findById(tagId);
			if (!tagEdit.isPresent() || !currentLoggedUser.getId().equals(tagEdit.get().getOwnerID())) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
