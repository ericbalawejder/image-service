package com.service.image.controller;

import com.service.image.entities.Image;
import com.service.image.exception.DuplicateImageException;
import com.service.image.response.ImageResponse;
import com.service.image.service.ImageServiceImpl;
import com.service.image.util.ImageUtility;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/photobin")
@CrossOrigin("http://localhost:4200")
public class ImageController {

    private final ImageServiceImpl imageService;

    public ImageController(ImageServiceImpl imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload/image")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam("image") MultipartFile multipartFile)
            throws IOException, NoSuchAlgorithmException, DuplicateImageException {

        imageService.uploadImage(multipartFile);

        return ResponseEntity
                .created(URI.create("/upload/image/" + multipartFile.getOriginalFilename()))
                .body(new ImageResponse(multipartFile.getOriginalFilename() + " uploaded successfully."));
    }

    @GetMapping(path = {"/get/image/info/{name}"})
    public ResponseEntity<Image> getImageDetails(@PathVariable("name") String name) {
        final Image image = imageService.getImageDetails(name);

        return ResponseEntity.ok()
                .body(image);
    }

    @GetMapping(path = {"/get/image/{name}"})
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) {
        final Image image = imageService.getImage(name);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(image.getType()))
                .body(ImageUtility.decompressImage(image.getPhoto()));
    }

    @GetMapping(path = {"/get/all/images"})
    public List<byte[]> getAllImages() {
        return imageService.getImages();
    }

    @DeleteMapping(path = {"/delete/image/{name}"})
    public ResponseEntity<ImageResponse> deleteImage(@PathVariable("name") String name) {
        imageService.deleteImage(name);

        return ResponseEntity.ok()
                .body(new ImageResponse(name + " deleted successfully."));
    }

}