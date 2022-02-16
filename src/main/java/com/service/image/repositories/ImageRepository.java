package com.service.image.repositories;

import java.util.Optional;

import com.service.image.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByName(String name);

    Optional<Image> findByHash(String hash);

}
