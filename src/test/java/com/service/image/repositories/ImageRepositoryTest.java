package com.service.image.repositories;

import com.service.image.entities.Image;
import com.service.image.util.ImageUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DataJpaTest
class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    private Image sunset;

    @BeforeEach
    void setup() {
        sunset = new Image(1L,
                "sunset.jpeg",
                "jpeg",
                100_000L,
                generateRandomString(44),
                ImageUtility.compressImage(new byte[]{127}));
    }

    @Test
    void testSaveImage() {
        final Image actual = imageRepository.save(sunset);

        assertNotNull(actual);
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void testGetImageByName() {
        imageRepository.save(sunset);
        final Optional<Image> actual = imageRepository.findByName("sunset.jpeg");

        assertThat(actual).isPresent();
    }

    @Test
    void findAllImages() {
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
        final List<Image> actual = imageRepository.findAll();
        final int expected = 2;

        assertNotNull(actual);
        assertThat(actual.size()).isEqualTo(expected);
    }

    @Test
    void testDeleteImageByName() {
        imageRepository.save(sunset);
        imageRepository.deleteById(sunset.getId());
        final Optional<Image> actual = imageRepository.findByName(sunset.getName());

        assertThat(actual).isEmpty();
    }

    private static String generateRandomString(int length) {
        return new SecureRandom().ints(length, 33, 127)
                .mapToObj(i -> (char) i)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

}