package com.service.image.hash;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HashTest {

    @Test
    void findTestResourceNamePathTest() {
        final String absolutePath = getTestFile("hope.jpeg");

        assertTrue(absolutePath.endsWith("/hope.jpeg"));
    }

    @Test
    void hashAndEncodeFileBase64Test() throws NoSuchAlgorithmException, IOException {
        final String absolutePath = getTestFile("hope.jpeg");
        final InputStream contentStream = new FileInputStream(absolutePath);
        final MockMultipartFile imageFile = new MockMultipartFile("hope.jpeg", contentStream);

        final String expected = "nRMQbr5Wxcaba2oBS0zOyvl2sGedxUOkiS3gg+27Uj4=";

        final String actual = Hash.encodeBytesBase64(imageFile);

        assertEquals(expected, actual);
    }

    private String getTestFile(String resourceName) {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(Objects.requireNonNull(classLoader.getResource(resourceName))
                .getFile());

        return file.getAbsolutePath();
    }

}