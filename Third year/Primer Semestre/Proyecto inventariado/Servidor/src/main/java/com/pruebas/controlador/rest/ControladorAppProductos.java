package com.pruebas.controlador.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebas.modelo.fotos.Foto;
import com.pruebas.modelo.fotos.FotoServiceImpl;
import com.pruebas.modelo.producto.Producto;
import com.pruebas.modelo.producto.ProductoDto;
import com.pruebas.modelo.producto.ProductoException;
import com.pruebas.modelo.producto.RepositorioProductos;
import com.pruebas.modelo.usuario.UserServiceImpl;
import com.pruebas.modelo.usuario.Usuario;

@RestController
@RequestMapping("/api/v1/productos")
@PreAuthorize("isAuthenticated()")
public class ControladorAppProductos {

	@Autowired
	private final RepositorioProductos repositorio;

	@Autowired
	private UserServiceImpl userRepository;

	private FotoServiceImpl fotoRepository;

	ControladorAppProductos(RepositorioProductos repositorio, FotoServiceImpl fotoRepository) {
		super();
		this.fotoRepository = fotoRepository;
		this.repositorio = repositorio;
	}

	/**
	 * Obtengo todos los productos
	 */
	@GetMapping("/all")
	public List<Producto> mostrarProductos() {
		Usuario currentLoggedUser = userRepository.getCurrentUser();
		return repositorio.getAllProducts(currentLoggedUser.getId());
	}

	/**
	 * Agrego un producto
	 */
	@PostMapping(value = "/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
			MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public Producto agregarProducto(@NotNull @RequestParam("Producto") String jsonProductoDto,
			@Nullable @RequestParam("file") MultipartFile... fotos) {

		ProductoDto productoDto = jsonToProductoDto(jsonProductoDto);

		if (!productoDto.isValidDto()) {
			throw new ProductoException("InvalidProduct");
		}

		if (fotos != null && fotos.length > 10) {
			throw new ProductoException("OverMaxPhotos");
		}

		try {

			Producto producto = repositorio.saveAndFlush(new Producto());

			Usuario user = userRepository.getCurrentUser();

			if (fotos != null && fotos.length > 0 && fotos.length < 11) {
				producto.setFotos(fotoRepository.saveAllProductImages(user, producto, fotos));
			}

			producto.setTags(productoDto.getTags());
			producto.setCantidad(productoDto.getCantidad());
			producto.setNombre(productoDto.getNombre());
			producto.setPrecio(productoDto.getPrecio());
			producto.setDescripcion(productoDto.getDescripcion());
			producto.setOwnerID(user.getId());
			producto.setMinLvlEnabled(Boolean.parseBoolean(productoDto.getMinLvlEnabled()));
			producto.setMinLvlOption(productoDto.getMinLvlOption());
			producto.setMinLvl(productoDto.getMinLvl());

			return repositorio.save(producto);
		} catch (Exception e) {
			throw new ProductoException("FailToCreate");
		}
	}

	/**
	 * Busco un producto
	 */
	@GetMapping("/search/{id}")
	public Producto buscarProducto(@PathVariable UUID id) {

		if (!isOwnerProduct(id)) {

			throw new ProductoException("InvalidProduct");
		}

		return repositorio.findById(id).orElseThrow(() -> new ProductoException("NoProductFound"));
	}

	@PostMapping("/edit/{id}")
	public Producto editarProducto(@NotNull @RequestParam("Producto") String jsonProductoDto,
			@NotNull @PathVariable UUID id, @Nullable @RequestParam("file") MultipartFile... fotos) {

		ProductoDto productoDto = jsonToProductoDto(jsonProductoDto);

		try {

			if (productoDto == null) {
				throw new ProductoException("NoProductFound");
			}

			if (!productoDto.isValidDto()) {
				throw new ProductoException("InvalidProduct");
			}

			if (fotos != null && fotos.length > 10) {
				throw new ProductoException("OverMaxPhotos");
			}

			if (!isOwnerProduct(id)) {
				throw new ProductoException("InvalidProduct");
			}

			Producto producto = repositorio.findById(id).get();
			Usuario user = userRepository.getCurrentUser();

			// ha agredado una nueva foto
			if (fotos != null && fotos.length > 0) {
				producto.setFotos(fotoRepository.saveAllProductImages(user, producto, fotos));

				// ha eliminado todas las fotos
			} else if (fotos != null && fotos.length == 0 && productoDto.getFileNames().isEmpty()) {
				fotoRepository.deleteAllImagesFromProduct(user.getId(), producto);
				producto.setFotos(null);

				// ha eliminado alguna foto
			} else if (!producto.getFotos().equals(productoDto.getFileNames().keySet())) {
				Set<String> photoNames = new HashSet<>();

				for (Foto foto : producto.getFotos()) {
					photoNames.add(foto.getNombreImagen());
				}

				HashSet<String> unionKeys = new HashSet<>(photoNames);

				unionKeys.addAll(productoDto.getFileNames().keySet());
				unionKeys.removeAll(productoDto.getFileNames().keySet());

				for (String fileName : unionKeys) {
					fotoRepository.deleteOneProductImage(user.getId(), producto, fileName);
					producto.removeFoto(fileName);
				}
			}

			producto.setTags(productoDto.getTags());
			producto.setCantidad(productoDto.getCantidad());
			producto.setNombre(productoDto.getNombre());
			producto.setPrecio(productoDto.getPrecio());
			producto.setMinLvlEnabled(Boolean.parseBoolean(productoDto.getMinLvlEnabled()));
			producto.setMinLvlOption(productoDto.getMinLvlOption());
			producto.setMinLvl(productoDto.getMinLvl());
			producto.setDescripcion(productoDto.getDescripcion());

			return repositorio.save(producto);
		} catch (Exception e) {
			throw new ProductoException("FailToSave");
		}
	}

	@DeleteMapping("/delete/{id}")
	public void eliminarProducto(@PathVariable UUID id) {

		if (!isOwnerProduct(id)) {
			throw new ProductoException("InvalidProduct");
		}

		Optional<Producto> producto = repositorio.findById(id);

		if (producto.isPresent()) {

			Usuario currentLoggedUser = userRepository.getCurrentUser();
			fotoRepository.deleteAllImagesFromProduct(currentLoggedUser.getId(), producto.get());
			repositorio.deleteById(id);
		}
	}

	@GetMapping("/alerts")
	public List<Producto> mostrarAlarmas() {
		Usuario currentLoggedUser = userRepository.getCurrentUser();
		return repositorio.getAllTriggeredProducts(currentLoggedUser.getId());
	}

	private ProductoDto jsonToProductoDto(String producto) {
		ProductoDto productoDto = new ProductoDto();

		try {
			ObjectMapper om = new ObjectMapper();
			om.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			productoDto = om.readValue(producto, ProductoDto.class);
		} catch (Exception e) {
			System.err.println("\n\n\nerror " + e.getMessage() + "\n\n\n");
			throw new ProductoException("InvalidProduct");
		}
		return productoDto;
	}

	private boolean isOwnerProduct(UUID productId) {
		if (productId == null) {
			return false;
		}
		try {
			Usuario currentLoggedUser = userRepository.getCurrentUser();
			Optional<Producto> productEdit = repositorio.findById(productId);
			if (!productEdit.isPresent() || !currentLoggedUser.getId().equals(productEdit.get().getOwnerID())) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
