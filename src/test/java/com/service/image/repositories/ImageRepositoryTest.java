package com.service.image.repositories;

import com.service.image.entities.Image;
import com.service.image.util.ImageUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    private Image sunset;

    @BeforeEach
    public void setup() {
        sunset = new Image(1L,
                "sunset.jpeg",
                "jpeg",
                100_000L,
                "hash",
                ImageUtility.compressImage(new byte[]{127}));
    }

    @Test
    public void testSaveImage() {
        final Image savedImage = imageRepository.save(sunset);

        assertNotNull(savedImage);
        assertThat(savedImage.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetImageByName() {
        imageRepository.save(sunset);
        final Optional<Image> imageDB = imageRepository.findByName("sunset.jpeg");

        assertThat(imageDB).isPresent();
    }

    @Test
    public void findAllImages() {
        final Image sky = new Image(2L,
                "sky.jpeg",
                "jpeg",
                200_000L,
                "skyhash",
                ImageUtility.compressImage(new byte[]{127}));

        final Image skull = new Image(3L,
                "skull.jpeg",
                "jpeg",
                300_000L,
                "skullhash",
                ImageUtility.compressImage(new byte[]{127}));

        imageRepository.save(sky);
        imageRepository.save(skull);
        final List<Image> images = imageRepository.findAll();

        assertNotNull(images);
        assertThat(images.size()).isEqualTo(2);
    }

    @Test
    public void testDeleteImageByName() {
        imageRepository.save(sunset);
        imageRepository.deleteById(sunset.getId());
        final Optional<Image> image = imageRepository.findByName(sunset.getName());

        assertThat(image).isEmpty();
    }

}