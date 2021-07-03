package com.pruebas.controlador.rest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pruebas.modelo.producto.Producto;
import com.pruebas.modelo.producto.RepositorioProductos;
import com.pruebas.modelo.tags.RepositorioTags;
import com.pruebas.modelo.tags.Tag;
import com.pruebas.modelo.tags.TagDto;
import com.pruebas.modelo.tags.TagException;
import com.pruebas.modelo.usuario.UserServiceImpl;
import com.pruebas.modelo.usuario.Usuario;

@RestController
@RequestMapping("/api/v1/tags")
@PreAuthorize("isAuthenticated()")
public class ControladorAppTags {

	@Autowired
	private final RepositorioTags repositorioTags;

	@Autowired
	private final RepositorioProductos repositorioProductos;

	@Autowired
	private UserServiceImpl userRepository;

	ControladorAppTags(RepositorioProductos repositorioProductos, RepositorioTags repositorioTags) {
		this.repositorioProductos = repositorioProductos;
		this.repositorioTags = repositorioTags;
	}

	@GetMapping("/all")
	public List<Tag> mostrarTags() {
		Usuario currentLoggedUser = userRepository.getCurrentUser();
		return repositorioTags.findAllByOwnerID(currentLoggedUser.getId());
	}

	@PostMapping("/add")
	public Tag agregarTag(@RequestBody String tagName) {
		try {

			if (StringUtils.isBlank(tagName)) {
				throw new TagException("InvalidName");
			}

			tagName = tagName.replaceAll("[^\\da-zA-Z ]", "");

			Usuario currentLoggedUser = userRepository.getCurrentUser();

			if (repositorioTags.findByNombre(currentLoggedUser.getId(), tagName).isPresent())
				throw new TagException("TagAlreadyExists");

			Tag newTag = new Tag(tagName, currentLoggedUser.getId());

			return repositorioTags.save(newTag);
		} catch (Exception e) {
			throw new TagException("FailSaveTag");
		}
	}

	@GetMapping("/search/{id}")
	public List<Producto> buscarTag(@PathVariable UUID id) {

		if (!isOwnerTag(id)) {
			throw new TagException("InvalidTag");
		}

		Usuario currentLoggedUser = userRepository.getCurrentUser();
		return repositorioProductos.getAllProductsWithTag(currentLoggedUser.getId(),
				repositorioTags.findById(id).get());

	}

	@PutMapping("/edit/{id}")
	public Tag editarTag(@RequestBody TagDto tag, @PathVariable UUID id) {

		try {
			if (tag == null || StringUtils.isBlank(tag.getNombre()) || id == null) {
				throw new TagException("InvalidParameters");
			}

			if (!isOwnerTag(id)) {
				throw new TagException("InvalidTag");
			}

			Usuario currentLoggedUser = userRepository.getCurrentUser();

			return repositorioTags.findById(currentLoggedUser.getId(), id).map(originalTag -> {
				originalTag.setNombre(tag.getNombre().replaceAll("[^\\da-zA-Z ]", ""));
				return repositorioTags.save(originalTag);
			}).orElseGet(() -> {
				return repositorioTags
						.save(new Tag(tag.getNombre().replaceAll("[^\\da-zA-Z ]", ""), id, currentLoggedUser.getId()));
			});
		} catch (Exception e) {
			throw new TagException("FailSaveTag");
		}
	}

	@DeleteMapping("/delete/{id}")
	public void eliminarTag(@PathVariable UUID id) {

		if (!isOwnerTag(id)) {
			throw new TagException("InvalidTag");
		}

		Optional<Tag> tagAEliminar = repositorioTags.findById(id);

		if (!tagAEliminar.isPresent()) {
			throw new TagException("TagNotFound");
		}

		Usuario currentLoggedUser = userRepository.getCurrentUser();
		repositorioProductos.removeTagFromProducts(currentLoggedUser.getId(), tagAEliminar.get());
		repositorioTags.deleteById(id);
	}

	private boolean isOwnerTag(UUID tagID) {
		if (tagID == null) {
			return false;
		}
		try {
			Usuario currentLoggedUser = userRepository.getCurrentUser();
			Optional<Tag> tagEdit = repositorioTags.findById(tagID);
			if (!tagEdit.isPresent() || !currentLoggedUser.getId().equals(tagEdit.get().getOwnerID())) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
