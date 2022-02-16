package com.service.image.controller;

import com.service.image.entities.Image;
import com.service.image.exception.ImageRepositoryException;
import com.service.image.exception.ImageSizeException;
import com.service.image.repositories.ImageRepository;
import com.service.image.response.ImageUploadResponse;
import com.service.image.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin()
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @PostMapping("/upload/image")
    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestParam("image") MultipartFile multipartFile)
            throws IOException {

        if (multipartFile.getSize() > 100_000_000L) throw new ImageSizeException();

        final Image image = new Image(multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                multipartFile.getSize(),
                "create hash",
                ImageUtility.compressImage(multipartFile.getBytes()));

        imageRepository.save(image);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ImageUploadResponse("Image uploaded successfully: " +
                        multipartFile.getOriginalFilename()));
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

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(image.getType()))
                .body(ImageUtility.decompressImage(image.getImage()));
    }

}