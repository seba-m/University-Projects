package com.pruebas.modelo.fotos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.pruebas.modelo.producto.Producto;
import com.pruebas.modelo.usuario.Usuario;

/**
 * Esta clase sirve para guardar imagenes de manera local.
 * 
 * @deprecated ya existe otra clase que hace lo mismo, pero en s3 de aws.
 */
@Deprecated
public class FileUploadUtil {

	private static final String USERPATH = "src\\main\\resources\\static\\img\\user-photos\\";
	private static final String PRODUCTPATH = "src\\main\\resources\\static\\img\\product-photos\\";
	private static final String DELIMIETER = "\\";

	public static void saveUserImage(UUID userID, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(USERPATH + userID);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);

			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + fileName, ioe);
		}
	}

	public static void deleteUserImage(UUID userID) {
		try {
			Path deletePath = Paths.get(USERPATH + userID);
			if (!Files.exists(deletePath)) {
				return;
			}
			FileUtils.cleanDirectory(deletePath.toFile());
			FileUtils.deleteDirectory(deletePath.toFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Set<Foto> saveAllProductImages(Usuario user, Producto producto, MultipartFile... allPhotos) {
		Set<Foto> fileNames = new HashSet<>(
				(producto.getFotos() != null) ? producto.getFotos() : Collections.emptySet());

		for (MultipartFile photo : allPhotos) {
			if (photo != null && !StringUtils.isBlank(photo.getOriginalFilename())) {
				try {
					String name = removeInvalidCharacters(photo.getOriginalFilename());
					Long size = photo.getSize();

					if (isValidPhoto(photo, user.getId().toString(), producto.getId().toString())) {
						saveProductImage(user.getId(), producto.getId(), name, photo);
						//fileNames.put(name, size);
					}

				} catch (IOException e) {
					// avisar que el archivo x no se pudo subir.
				}
			}
		}
		return fileNames;
	}

	public static void saveProductImage(UUID userID, UUID productId, String fileName, MultipartFile multipartFile)
			throws IOException {
		Path uploadPath = Paths.get(PRODUCTPATH + userID + DELIMIETER + productId);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);

			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + fileName, ioe);
		}
	}

	public static void deleteProductImage(UUID ownerID, UUID productID) {
		try {
			Path deletePath = Paths.get(PRODUCTPATH + ownerID + DELIMIETER + productID);
			if (!Files.exists(deletePath)) {
				return;
			}

			Files.delete(deletePath);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteOneProductImage(UUID ownerID, UUID productID, String photoName) {
		try {
			Path deletePath = Paths.get(PRODUCTPATH + ownerID + DELIMIETER + productID + DELIMIETER + photoName);
			if (!Files.exists(deletePath)) {
				return;
			}

			Files.delete(deletePath);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteAllImagesFromProduct(UUID ownerID, UUID productID) {
		try {
			Path deletePath = Paths.get(PRODUCTPATH + ownerID + DELIMIETER + productID);
			if (!Files.exists(deletePath)) {
				return;
			}
			FileUtils.cleanDirectory(deletePath.toFile());
			FileUtils.deleteDirectory(deletePath.toFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean isValidPhoto(MultipartFile fileToCheck, String userID, String productID) {
		return isPhoto(fileToCheck) && !photoExists(userID, productID, fileToCheck);
	}

	private static boolean photoExists(String userID, String productID, MultipartFile fileToCheck) {

		if (fileToCheck != null && !StringUtils.isBlank(fileToCheck.getOriginalFilename())) {

			try {
				Path photoPath = Paths
						.get(PRODUCTPATH + userID + DELIMIETER + productID + DELIMIETER + fileToCheck.getName());
				if (Files.exists(photoPath)) {
					return true;
				}

				Path photosFolder = Paths.get(PRODUCTPATH + userID + DELIMIETER + productID);

				String fileName = fileToCheck.getOriginalFilename();

				String filePrefix = fileName.substring(fileName.lastIndexOf("."));

				File tmpFile = File.createTempFile(fileName, filePrefix);

				fileToCheck.transferTo(tmpFile);

				for (File file : photosFolder.toFile().listFiles()) {
					if (FileUtils.contentEquals(file, tmpFile)) {
						Files.delete(Paths.get(tmpFile.toURI()));
						return true;
					}
				}
			} catch (Exception noExisteLaFoto) {
				return false;
			}

		}
		return false;
	}

	private static boolean isPhoto(MultipartFile fileToCheck) {
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
		return Pattern.compile("[:*<>\\/|?\"]+").matcher(fileName).replaceAll("");
	}
}
