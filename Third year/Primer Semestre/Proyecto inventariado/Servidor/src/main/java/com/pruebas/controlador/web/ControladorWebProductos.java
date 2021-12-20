package com.pruebas.controlador.web;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.pruebas.modelo.fotos.Foto;
import com.pruebas.modelo.fotos.FotoServiceImpl;
import com.pruebas.modelo.producto.Producto;
import com.pruebas.modelo.producto.ProductoDto;
import com.pruebas.modelo.producto.RepositorioProductos;
import com.pruebas.modelo.tags.RepositorioTags;
import com.pruebas.modelo.usuario.UserServiceImpl;
import com.pruebas.modelo.usuario.Usuario;

@Controller
public class ControladorWebProductos {

	@Autowired
	private final RepositorioProductos repositorio;

	@Autowired
	private final RepositorioTags repositorioTags;

	@Autowired
	private UserServiceImpl userRepository;

	private FotoServiceImpl fotoRepository;

	@Value("${aws.bucket}")
	private String bucketName;

	ControladorWebProductos(RepositorioProductos repositorio, RepositorioTags repositorioTags,
			FotoServiceImpl fotoRepository) {
		super();
		this.fotoRepository = fotoRepository;
		this.repositorio = repositorio;
		this.repositorioTags = repositorioTags;
	}

	@ModelAttribute("Producto")
	public ProductoDto ProductoDto() {
		return new ProductoDto();
	}

	@GetMapping("/app/inventory/item/new")
	public String productAdd(Model md) {
		Usuario currentLoggedUser = userRepository.getCurrentUser();
		md.addAttribute("AllTags", repositorioTags.findAllByOwnerID(currentLoggedUser.getId()));
		md.addAttribute("Photo",
				"https://inventarispro.s3.amazonaws.com/" + currentLoggedUser.getPhoto().getUbicacionImagen()
						+ (!currentLoggedUser.isDefault() ? "/avatar.png" : "/avatardefault.png"));

		List<Producto> triggered = repositorio.getAllTriggeredProducts(currentLoggedUser.getId());

		String alertAmmount = null;

		if (triggered != null && !triggered.isEmpty()) {
			alertAmmount = (triggered.size() > 9) ? "9+" : String.valueOf(triggered.size());
		}

		md.addAttribute("AlertAmmount", alertAmmount);

		return "productCreation";
	}

	@PostMapping(value = "/app/inventory/item/new", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
			MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public String productAddPost(MultipartHttpServletRequest files, @ModelAttribute ProductoDto productoDto) {

		try {

			if (productoDto == null)
				return "redirect:/app/inventory?fail=NoProduct";

			if (!productoDto.isValidDto())
				return "redirect:/app/inventory?fail=InvalidProduct";

			if (files != null && files.getFileMap().size() > 10)
				return "redirect:/app/inventory?fail=OverMaxPhotos";

			Producto producto = repositorio.saveAndFlush(new Producto());

			Usuario user = userRepository.getCurrentUser();

			if (files != null && files.getFileMap().size() > 0 && files.getFileMap().size() < 11) {
				producto.setFotos(fotoRepository.saveAllProductImages(user, producto,
						files.getFileMap().values().toArray(new MultipartFile[0])));
			}

			producto.setTags(productoDto.getTags());
			producto.setCantidad(productoDto.getCantidad());
			producto.setNombre(productoDto.getNombre());
			producto.setPrecio(productoDto.getPrecio());
			producto.setMinLvlEnabled(Boolean.parseBoolean(productoDto.getMinLvlEnabled()));
			producto.setMinLvlOption(productoDto.getMinLvlOption());
			producto.setMinLvl(productoDto.getMinLvl());
			producto.setDescripcion(productoDto.getDescripcion());
			producto.setOwnerID(user.getId());

			repositorio.save(producto);

			return "redirect:/app/inventory?success=Creation";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/app/inventory?fail=Creation";
		}
	}

	@GetMapping("/app/inventory/item/edit/{id}")
	public String productEdit(Model md, @PathVariable UUID id) {

		if (!isOwnerProduct(id)) {
			return "redirect:/app/inventory?fail=WrongProduct";
		}

		Usuario currentLoggedUser = userRepository.getCurrentUser();

		Producto producto = repositorio.findById(id).get();

		md.addAttribute("AllTags", repositorioTags.findAllByOwnerID(currentLoggedUser.getId()));
		md.addAttribute("productoAEditar", producto);
		md.addAttribute("photoLink", "\"https://" + bucketName + ".s3.amazonaws.com/product-photos/"
				+ producto.getOwnerID() + "/" + producto.getId() + "/\"");
		md.addAttribute("fileMap", producto.getAllFotosName());
		md.addAttribute("Photo",
				"https://inventarispro.s3.amazonaws.com/" + currentLoggedUser.getPhoto().getUbicacionImagen()
						+ (!currentLoggedUser.isDefault() ? "/avatar.png" : "/avatardefault.png"));

		List<Producto> triggered = repositorio.getAllTriggeredProducts(currentLoggedUser.getId());

		String alertAmmount = null;

		if (triggered != null && !triggered.isEmpty()) {
			alertAmmount = (triggered.size() > 9) ? "9+" : String.valueOf(triggered.size());
		}

		md.addAttribute("AlertAmmount", alertAmmount);

		return "productEdit";
	}

	@PostMapping(value = "/app/inventory/item/edit/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
			MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public String productEditPost(MultipartHttpServletRequest files, @ModelAttribute ProductoDto productoDto,
			@PathVariable UUID id) {

		try {
			if (!isOwnerProduct(id)) {
				return "redirect:/app/inventory?fail=WrongProduct";
			}

			if (productoDto == null) {
				return "redirect:/app/inventory?fail=NoProduct";
			}

			if (files != null && files.getFileMap().size() > 10)
				return "redirect:/app/inventory?fail=OverMaxPhotos";

			Producto producto = repositorio.findById(id).get();
			Usuario user = userRepository.getCurrentUser();

			// ha agredado una nueva foto
			if (files != null && files.getFileMap().size() > 0) {

				producto.setFotos(fotoRepository.saveAllProductImages(user, producto,
						files.getFileMap().values().toArray(new MultipartFile[0])));

				// ha eliminado todas las fotos
			} else if (files != null && files.getFileMap().size() == 0 && productoDto.getFileNames().isEmpty()) {
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

			repositorio.save(producto);

			return "redirect:/app/inventory?success=Edit";
		} catch (Exception e) {
			return "redirect:/app/inventory?fail=Edit";
		}
	}

	@GetMapping("/app/inventory/item/delete/{id}")
	public String productDelete(@PathVariable UUID id) {
		if (!isOwnerProduct(id)) {
			return "redirect:/app/inventory?fail=ParamNull";
		}

		Optional<Producto> producto = repositorio.findById(id);

		if (producto.isPresent()) {
			Usuario currentLoggedUser = userRepository.getCurrentUser();
			fotoRepository.deleteAllImagesFromProduct(currentLoggedUser.getId(), producto.get());
			repositorio.deleteById(id);
			return "redirect:/app/inventory?success=Delete";
		} else {
			return "redirect:/app/inventory?fail=NoItemFound";
		}
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
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
