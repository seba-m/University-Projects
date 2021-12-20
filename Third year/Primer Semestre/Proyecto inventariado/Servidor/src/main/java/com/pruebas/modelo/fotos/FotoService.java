package com.pruebas.modelo.fotos;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.pruebas.modelo.producto.Producto;
import com.pruebas.modelo.usuario.Usuario;

public interface FotoService {
	Foto saveUserImage(UUID userID, MultipartFile multipartFile) throws IOException;

	void deleteUserImage(Usuario user);

	Set<Foto> saveAllProductImages(Usuario user, Producto producto, MultipartFile... allPhotos);

	Foto saveProductImage(UUID userID, UUID productId, String fileName, MultipartFile multipartFile);

	void deleteOneProductImage(UUID ownerID, Producto producto, String photoName);

	void deleteAllImagesFromProduct(UUID ownerID, Producto producto);
}
