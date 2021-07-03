package com.pruebas.modelo.fotos;

import static org.apache.http.entity.ContentType.IMAGE_BMP;
import static org.apache.http.entity.ContentType.IMAGE_GIF;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.pruebas.modelo.producto.Producto;
import com.pruebas.modelo.usuario.Usuario;

@Service
public class FotoServiceImpl implements FotoService {

	private final FotoUtils fileStore;
	private final RepositorioFotos repository;
	@Value("${aws.bucket}")
	private String bucketName;

	public FotoServiceImpl(FotoUtils fileStore, RepositorioFotos repository) {
		super();
		this.fileStore = fileStore;
		this.repository = repository;
	}

	@Async
	private Foto saveFoto(String location, MultipartFile file) {
		if (!isValidPhoto(file)) {
			throw new IllegalStateException("Cannot upload file");
		}

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(file.getSize());

		UUID title = UUID.randomUUID();

		String path = bucketName + location;
		String fileName = removeInvalidCharacters(file.getOriginalFilename());
		try {
			fileStore.uploadSingleFile(path, fileName, metadata, file.getInputStream());
		} catch (IOException e) {
			throw new IllegalStateException("Failed to upload file", e);
		}

		return repository.save(new Foto(title.toString(), fileName, location, file.getSize()));
	}

	@Async
	private Foto saveUserFoto(String location, MultipartFile file) {
		if (!isValidPhoto(file)) {
			throw new IllegalStateException("Cannot upload file");
		}

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(file.getSize());

		UUID title = UUID.randomUUID();
		String path = bucketName + "/" + location;

		try {
			fileStore.uploadSingleFile(path, "avatar.png", metadata, file.getInputStream());
		} catch (IOException e) {
			throw new IllegalStateException("Failed to upload file", e);
		}

		return repository.save(new Foto(title.toString(), "avatar.png", location, file.getSize()));
	}

	@Override
	@Async
	public Foto saveUserImage(UUID userID, MultipartFile multipartFile) throws IOException {
		String location = String.format(FotoPath.USER.getPath(), userID.toString());

		try {
			return saveUserFoto(location, multipartFile);
		} catch (Exception failSavingPhoto) {
			throw new IOException(failSavingPhoto.getMessage());
		}

	}

	@Override
	@Async
	public void deleteUserImage(Usuario user) {
		if (!user.isDefault()) {
			String location = String.format(FotoPath.USER.getPath(), user.getId().toString(), "avatar.png");
			fileStore.removeSingleImage(bucketName, location);
		}
	}

	@Override
	@Async
	public Set<Foto> saveAllProductImages(Usuario user, Producto producto, MultipartFile... allPhotos) {
		return fileStore.uploadMultipleFiles(user, producto, bucketName, allPhotos);
	}

	@Override
	@Async
	public Foto saveProductImage(UUID userID, UUID productId, String fileName, MultipartFile multipartFile)
			throws IllegalStateException {
		String location = String.format(FotoPath.PRODUCT.getPath(), userID.toString(), productId.toString());
		return saveFoto(location, multipartFile);
	}

	@Override
	@Async
	public void deleteOneProductImage(UUID ownerID, Producto producto, String photoName) {
		String location = String.format(FotoPath.PRODUCT_FILE.getPath(), ownerID.toString(),
				producto.getId().toString(), photoName);
		fileStore.removeSingleImage(bucketName, location);
	}

	@Override
	@Async
	public void deleteAllImagesFromProduct(UUID ownerID, Producto producto) {
		if (producto.getFotos() != null && !producto.getFotos().isEmpty()) {
			List<String> filePaths = new ArrayList<>();

			for (String name : producto.getAllFotosName().keySet()) {
				String location = String.format(FotoPath.PRODUCT_FILE.getPath(), ownerID.toString(),
						producto.getId().toString(), name);
				filePaths.add(location);
			}

			fileStore.deleteDirectory(bucketName, filePaths);
		}
	}

	@Async
	public Foto saveAndFlush(Foto f) {
		return repository.saveAndFlush(f);
	}

	private boolean isValidPhoto(MultipartFile fileToCheck) {
		if (fileToCheck == null)
			return false;

		return isPhoto(fileToCheck) && !fileStore.fileExists(bucketName, fileToCheck.getName());
	}

	private static boolean isPhoto(MultipartFile fileToCheck) {

		if (fileToCheck == null) {
			return false;
		}

		if (!Arrays.asList(IMAGE_PNG.getMimeType(), IMAGE_BMP.getMimeType(), IMAGE_GIF.getMimeType(),
				IMAGE_JPEG.getMimeType()).contains(fileToCheck.getContentType())) {
			return false;
		}

		try {
			ImageIO.read(fileToCheck.getInputStream());
			return true;
		} catch (IOException thisFileIsNotAPhoto) {
			return false;
		}
	}

	private static String removeInvalidCharacters(String fileName) {
		if (fileName == null) {
			return null;
		}

		if (fileName.matches("[^0-9A-Z-a-z]")) {
			return fileName.substring(0, fileName.lastIndexOf('.')).replaceAll("[^0-9A-Z-a-z]", "");
		} else {
			return fileName;
		}
	}

}
