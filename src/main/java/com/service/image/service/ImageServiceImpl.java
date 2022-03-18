package com.service.image.service;

import com.service.image.entities.Image;
import com.service.image.exception.DuplicateImageException;
import com.service.image.exception.ImageRepositoryException;
import com.service.image.exception.ImageSizeException;
import com.service.image.hash.Hash;
import com.service.image.repositories.ImageRepository;
import com.service.image.util.ImageUtility;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public void uploadImage(MultipartFile multipartFile) throws NoSuchAlgorithmException, IOException {
        if (multipartFile.getSize() > 100_000_000L) {
            throw new ImageSizeException();
        }
        final Image image = new Image(multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                multipartFile.getSize(),
                Hash.encodeBytesBase64(multipartFile),
                ImageUtility.compressImage(multipartFile.getBytes()));

        try {
            imageRepository.save(image);
        } catch (Exception e) {
            throw new DuplicateImageException();
        }
    }

    @Override
    public Image getImage(String name) {
        return imageRepository.findByName(name)
                .orElseThrow(ImageRepositoryException::new);
    }

    @Override
    public Image getImageDetails(String name) {
        return imageRepository.findByName(name)
                .orElseThrow(ImageRepositoryException::new);
    }

    @Override
    public List<byte[]> getImages() {
        return imageRepository.findAll()
                .stream()
                .map(Image::getPhoto)
                .map(ImageUtility::decompressImage)
                .toList();
    }

    @Override
    public void deleteImage(String name) {
        final Image image = imageRepository.findByName(name)
                .orElseThrow(ImageRepositoryException::new);

        imageRepository.delete(image);
    }

}
