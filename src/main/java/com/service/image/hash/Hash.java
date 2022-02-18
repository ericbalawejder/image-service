package com.service.image.hash;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Hash {

    // https://docs.oracle.com/en/java/javase/17/docs/specs/security/standard-names.html
    private static final String HASHING_ALGORITHM = "SHA-256";

    public static String encodeFileBase64(MultipartFile multipartFile) throws NoSuchAlgorithmException {
        final MessageDigest messageDigest = MessageDigest.getInstance(HASHING_ALGORITHM);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            final byte[] bytes = new byte[1024];
            for (int length = inputStream.read(bytes); length != -1; length = inputStream.read(bytes)) {
                messageDigest.update(bytes, 0, length);
            }
        } catch (IOException ignored) {
        }
        return Base64.getEncoder().encodeToString(messageDigest.digest());
    }

}
