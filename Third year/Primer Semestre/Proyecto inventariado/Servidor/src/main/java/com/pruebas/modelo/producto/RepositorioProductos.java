package com.pruebas.modelo.producto;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pruebas.modelo.fotos.Foto;
import com.pruebas.modelo.tags.Tag;

@Repository
public interface RepositorioProductos extends JpaRepository<Producto, UUID> {

	@Override
	@SuppressWarnings("unchecked")
	Producto save(Producto producto);

	@Override
	List<Producto> findAll();

	@Override
	Optional<Producto> findById(UUID id);

	List<Producto> findByTags(Tag tags);

	@Query(value = "SELECT * FROM producto WHERE ownerid = :id and min_lvl_triggered = true", nativeQuery = true)
	List<Producto> getAllTriggeredProducts(@Param("id") UUID ownerID);

	@Override
	void deleteById(UUID id);

	default void removeTagFromProducts(UUID ownerID, Tag tagToRemove) {

		List<Producto> productos = getAllProducts(ownerID);

		for (Iterator<Producto> iterator = productos.iterator(); iterator.hasNext();) {
			Producto producto = iterator.next();

			if (producto.getTags().contains(tagToRemove)) {
				producto.getTags().remove(tagToRemove);
			}
		}

		saveAll(productos);
	}

	@Query(value = "SELECT * FROM producto WHERE ownerid = :ownerID and id = :productID", nativeQuery = true)
	Optional<Producto> getProduct(@Param("ownerID") UUID ownerID, @Param("productID") UUID productID);

	default List<Producto> getAllProductsWithTag(UUID ownerID, Tag tag) {
		List<Producto> productos = getAllProducts(ownerID);

		if (productos == null || productos.isEmpty()) {
			return Collections.emptyList();
		}

		List<Producto> productosConTag = new ArrayList<>();

		for (Producto producto : productos) {
			if (producto.containsTag(tag)) {
				productosConTag.add(producto);
			}
		}

		return productosConTag;
	}

	default Set<Foto> getAllPhotosProducts(UUID ownerID, UUID productID) {
		Optional<Producto> item = getProduct(ownerID, productID);
		if (item.isPresent()) {
			return item.get().getFotos();
		} else {
			return Collections.emptySet();
		}
	}

	@Query(value = "SELECT * FROM producto WHERE ownerid = :id", nativeQuery = true)
	List<Producto> getAllProducts(@Param("id") UUID ownerID);

	default String getStockAmmountByUser(UUID userId) {
		List<Producto> items = getAllProducts(userId);
		BigInteger stockTotal = BigInteger.valueOf(0);
		NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
		fmt.setMaximumFractionDigits(2);

		for (Producto producto : items) {
			try {
				String ammount = producto.getCantidad();
				stockTotal = stockTotal.add(new BigInteger(ammount));
			} catch (NumberFormatException e) {
				return "Max Quantity Reached.";
			}
		}
		return fmt.format(stockTotal);
	}

	default String getTotalAmmountByUser(UUID userId) {

		List<Producto> items = getAllProducts(userId);
		BigInteger cantidadTotal = BigInteger.valueOf(0);
		NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
		fmt.setMaximumFractionDigits(2);

		for (Producto producto : items) {
			try {
				String ammount = producto.getPrecio();
				cantidadTotal = cantidadTotal.add(new BigInteger(ammount));
			} catch (NumberFormatException e) {
				return "Max Ammount Reached.";
			}
		}
		return fmt.format(cantidadTotal);
	}

	@Query(value = "SELECT * FROM producto WHERE ownerid = :id ORDER BY update_date DESC LIMIT 5", nativeQuery = true)
	List<Producto> getRecentEditProducts(@Param("id") UUID ownerID);
}
