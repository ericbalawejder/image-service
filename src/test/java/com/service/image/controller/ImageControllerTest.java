package com.service.image.controller;

import com.service.image.entities.Image;
import com.service.image.response.ImageResponse;
import com.service.image.util.ImageUtility;
import org.hamcrest.core.Is;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
public class ImageControllerTest {

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

        final ResponseEntity<ImageResponse> imageResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(new ImageResponse("Image uploaded successfully: " + image.getName()));

        when(imageController.uploadImage(imageFile)).thenReturn(imageResponse);

        final MvcResult mvcResult = mockMvc.perform(multipart("/upload/image")
                        .file(imageFile)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void givenImageGetImageDetails() throws Exception {
        given(imageController.getImageDetails("sky.jpeg")).willReturn(image);

        final MvcResult mvcResult = mockMvc.perform(get("/get/image/info/" + image.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Is.is(image.getId())))
                .andExpect(jsonPath("$.name", Is.is(image.getName())))
                .andExpect(jsonPath("$.type", Is.is(image.getType())))
                .andExpect(jsonPath("$.size", Is.is(image.getSize())))
                .andExpect(jsonPath("$.hash", Is.is(image.getHash())))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

}
