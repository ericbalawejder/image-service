package com.service.image.service;

import com.service.image.entities.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ImageService {

    void uploadImage(MultipartFile multipartFile) throws NoSuchAlgorithmException, IOException;

    Image getImage(String name);

    Image getImageDetails(String name);

    List<byte[]> getImages();

    void deleteImage(String name);

}
