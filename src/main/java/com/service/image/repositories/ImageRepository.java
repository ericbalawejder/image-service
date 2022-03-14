package com.service.image.repositories;

import com.service.image.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByName(String name);

}
