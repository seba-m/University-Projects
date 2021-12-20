package com.pruebas.modelo.fotos;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.pruebas.modelo.producto.Producto;
import com.pruebas.modelo.usuario.Usuario;

@Service
public class FotoUtils {
	@Autowired
	private final AmazonS3 amazonS3;

	@Autowired
	private final RepositorioFotos repository;

	FotoUtils(AmazonS3 amazonS3, RepositorioFotos repository) {
		this.amazonS3 = amazonS3;
		this.repository = repository;
	}

	public void uploadSingleFile(String path, String fileName, ObjectMetadata meta, InputStream inputStream) {
		try {
			amazonS3.putObject(path, fileName, inputStream, meta);
		} catch (AmazonServiceException e) {
			e.printStackTrace();
			throw new IllegalStateException("Failed to upload the file", e);
		}
	}

	public Set<Foto> uploadMultipleFiles(Usuario owner, Producto producto, String bucketName,
			MultipartFile... allPhotos) {

		Set<Foto> fotos = new HashSet<>((producto.getFotos() != null) ? producto.getFotos() : Collections.emptySet());

		TransferManager tf = TransferManagerBuilder.standard().withS3Client(amazonS3).build();
		List<PutObjectRequest> fileList = new ArrayList<>();

		for (MultipartFile foto : allPhotos) {

			String photoType = foto.getContentType();

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(photoType);
			metadata.setContentLength(foto.getSize());

			UUID title = UUID.randomUUID();
			String fileName = removeInvalidCharacters(foto.getOriginalFilename());// + ".png";
			String location = String.format(FotoPath.PRODUCT.getPath(), owner.getId().toString(),
					producto.getId().toString());
			try {
				fileList.add(new PutObjectRequest(bucketName + location, fileName, foto.getInputStream(), metadata));
				fotos.add(saveAndFlush(new Foto(title.toString(), fileName, location, foto.getSize())));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		CountDownLatch doneSignal = new CountDownLatch(fileList.size());
		ArrayList<Upload> uploads = new ArrayList<>();
		for (PutObjectRequest object : fileList) {
			object.setGeneralProgressListener(
					new UploadCompleteListener(object.getFile(), object.getBucketName() + object.getKey(), doneSignal));
			uploads.add(tf.upload(object));

		}
		try {
			doneSignal.await();
		} catch (InterruptedException e) {
		}

		tf.shutdownNow(false);

		return fotos;
	}

	/**
	 * @param bucketName = bucketExample
	 * @param photoName  = path/to/file.jpg
	 */
	public boolean fileExists(String bucketName, String photoName) {
		try {
			return amazonS3.doesObjectExist(bucketName, photoName);
		} catch (Exception error) {
			error.printStackTrace();
			return false;
		}

	}

	public void removeSingleImage(String bucketName, String keyName) {
		try {
			amazonS3.deleteObject(new DeleteObjectRequest(bucketName, keyName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteDirectory(String bucketName, List<String> keys) {
		try {

			List<KeyVersion> bulk = new ArrayList<>();

			for (String key : keys) {
				bulk.add(new KeyVersion(key));
			}

			DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucketName).withKeys(bulk)
					.withQuiet(false);

			amazonS3.deleteObjects(multiObjectDeleteRequest);
		} catch (AmazonServiceException e) {
			e.printStackTrace();
		} catch (SdkClientException e1) {
			e1.printStackTrace();
		}
	}

	public Foto saveAndFlush(Foto f) {
		return repository.saveAndFlush(f);
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
