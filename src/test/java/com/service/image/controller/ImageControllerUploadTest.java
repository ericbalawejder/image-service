package com.service.image.controller;

import com.service.image.entities.Image;
import com.service.image.response.ImageUploadResponse;
import com.service.image.util.ImageUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
public class ImageControllerUploadTest {

    @MockBean
    private ImageController imageController;

    @Autowired
    private MockMvc mockMvc;

    private Image image;

    @BeforeEach
    void setUp() {
        image = new Image(100_000_000_000L,
                "sky.jpeg",
                "jpeg",
                900_000_000_000L,
                "hash",
                ImageUtility.compressImage(new byte[]{1, 2, 3, 4}));
    }

    @Test
    public void whenUploadingImageThenReturnResponse() throws Exception {
        final MockMultipartFile imageFile = new MockMultipartFile("image", new byte[]{127});

        final ResponseEntity<ImageUploadResponse> imageUploadResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(new ImageUploadResponse("Image uploaded successfully: " + image.getName()));

        when(imageController.uploadImage(imageFile)).thenReturn(imageUploadResponse);

        final MvcResult mvcResult = mockMvc.perform(multipart("/upload/image")
                        .file(imageFile)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

}
