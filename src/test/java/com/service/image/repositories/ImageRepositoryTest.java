package com.service.image.repositories;

import com.service.image.entities.Image;
import com.service.image.exception.ImageRepositoryException;
import com.service.image.util.ImageUtility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    @Test
    public void testSaveImage() {
        Image image = new Image(1L,
                "sunset",
                "jpeg",
                100_000L,
                "hash",
                ImageUtility.compressImage(new byte[]{8}));

        imageRepository.save(image);
        Image retrievedImage = imageRepository.findByName("sunset")
                .orElseThrow(ImageRepositoryException::new);
        assertNotNull(image);
        assertEquals(retrievedImage.getName(), image.getName());
        assertEquals(retrievedImage.getHash(), image.getHash());
    }

    @Test
    public void testGetImage() {
        Image image = new Image(1L,
                "sunset",
                "jpeg",
                100_000L,
                "hash",
                ImageUtility.compressImage(new byte[]{8}));

        imageRepository.save(image);
        Image retrievedImage = imageRepository.findByName("sunset")
                .orElseThrow(ImageRepositoryException::new);
        assertNotNull(image);
        assertEquals(retrievedImage.getName(), image.getName());
        assertEquals(retrievedImage.getHash(), image.getHash());
    }

    @Test
    public void testDeleteImage() {
        Image image = new Image(1L,
                "sunset",
                "jpeg",
                100_000L,
                "hash",
                ImageUtility.compressImage(new byte[]{8}));

        imageRepository.save(image);
        imageRepository.delete(image);
    }

    @Test
    public void findAllImages() {
        Image image = new Image(1L,
                "sunset",
                "jpeg",
                100_000L,
                "hash",
                ImageUtility.compressImage(new byte[]{8}));

        imageRepository.save(image);
        assertNotNull(imageRepository.findAll());
    }

    @Test
    public void deleteByImageIdTest() {
        Image image = new Image(1L,
                "sunset",
                "jpeg",
                100_000L,
                "hash",
                ImageUtility.compressImage(new byte[]{8}));

        Image temp = imageRepository.save(image);
        imageRepository.deleteById(temp.getId());
    }

}