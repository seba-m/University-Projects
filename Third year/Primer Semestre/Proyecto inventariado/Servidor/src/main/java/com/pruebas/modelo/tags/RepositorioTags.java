package com.pruebas.modelo.tags;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioTags extends JpaRepository<Tag, UUID> {

	@Override
	@SuppressWarnings("unchecked")
	Tag save(Tag Tag);

	@Override
	List<Tag> findAll();

	@Override
	Optional<Tag> findById(UUID id);

	@Override
	void deleteById(UUID id);

	@Query(value = "SELECT * FROM tag WHERE ownerid = :id and nombre = :name", nativeQuery = true)
	Optional<Tag> findByNombre(@Param("id") UUID ownerID, @Param("name") String name);

	@Query(value = "SELECT * FROM tag WHERE ownerid = :id and id = :tagID", nativeQuery = true)
	Optional<Tag> findById(@Param("id") UUID ownerID, @Param("tagID") UUID id);

	@Query(value = "SELECT * FROM tag WHERE ownerid = :id", nativeQuery = true)
	List<Tag> findAllByOwnerID(@Param("id") UUID ownerID);

}
