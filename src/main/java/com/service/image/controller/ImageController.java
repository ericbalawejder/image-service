package com.service.image.controller;

import com.service.image.entities.Image;
import com.service.image.exception.DuplicateImageException;
import com.service.image.exception.ImageRepositoryException;
import com.service.image.exception.ImageSizeException;
import com.service.image.hash.Hash;
import com.service.image.repositories.ImageRepository;
import com.service.image.response.ImageResponse;
import com.service.image.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@CrossOrigin()
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @PostMapping("/upload/image")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam("image") MultipartFile multipartFile)
            throws IOException, NoSuchAlgorithmException, DuplicateImageException {

        if (multipartFile.getSize() > 100_000_000L) throw new ImageSizeException();

        final Image image = new Image(multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                multipartFile.getSize(),
                Hash.encodeFileBase64(multipartFile),
                ImageUtility.compressImage(multipartFile.getBytes()));

        try {
            imageRepository.save(image);
        } catch (Exception e) {
            throw new DuplicateImageException();
        }
        final ImageResponse imageResponse = new ImageResponse(
                "Image " + multipartFile.getOriginalFilename() + " uploaded successfully");

        return ResponseEntity.ok()
                .body(imageResponse);
    }

    @GetMapping(path = {"/get/image/info/{name}"})
    public Image getImageDetails(@PathVariable("name") String name) {
        final Image image = imageRepository.findByName(name)
                .orElseThrow(ImageRepositoryException::new);

        return new Image(image.getId(),
                image.getName(),
                image.getType(),
                image.getSize(),
                image.getHash(),
                ImageUtility.decompressImage(image.getImage()));
    }

    @GetMapping(path = {"/get/image/{name}"})
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) {
        final Image image = imageRepository.findByName(name)
                .orElseThrow(ImageRepositoryException::new);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(image.getType()))
                .body(ImageUtility.decompressImage(image.getImage()));
    }

    @DeleteMapping(path = {"/delete/image/{name}"})
    public ResponseEntity<ImageResponse> deleteImage(@PathVariable("name") String name) {
        final Image image = imageRepository.findByName(name)
                .orElseThrow(ImageRepositoryException::new);

        imageRepository.delete(image);

        final ImageResponse imageResponse = new ImageResponse(
                "Image " + image.getName() + " deleted successfully.");

        return ResponseEntity.ok()
                .body(imageResponse);
    }

}