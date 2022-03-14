package com.service.image.controller;

import com.service.image.entities.Image;
import com.service.image.exception.DuplicateImageException;
import com.service.image.exception.ImageRepositoryException;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.security.SecureRandom;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(ImageController.class)
class ImageControllerTest {

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
                generateRandomString(44),
                ImageUtility.compressImage(new byte[]{1, 2, 3, 4}));
    }

    @Test
    void whenUploadingImageThenReturnResponse() throws Exception {
        final MockMultipartFile imageFile = new MockMultipartFile("image", new byte[]{127});

        final ResponseEntity<ImageResponse> imageResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(new ImageResponse("Image uploaded successfully: " + image.getName()));

        when(imageController.uploadImage(imageFile)).thenReturn(imageResponse);

        final MvcResult mvcResult = mockMvc.perform(multipart("/photobin/upload/image")
                        .file(imageFile)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void whenUploadingDuplicateImageThenReturnImageErrorResponse() throws Exception {
        final MockMultipartFile imageFile = new MockMultipartFile("image", new byte[]{127});

        when(imageController.uploadImage(imageFile)).thenThrow(new DuplicateImageException());

        final MvcResult mvcResult = mockMvc.perform(multipart("/photobin/upload/image")
                        .file(imageFile)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", Is.is("image is already on file")))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void givenImageGetImageDetails() throws Exception {
        given(imageController.getImageDetails("sky.jpeg"))
                .willReturn(ResponseEntity.ok().body(image));

        final MvcResult mvcResult = mockMvc.perform(get("/photobin/get/image/info/" + image.getName()))
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

    @Test
    void givenImageDetailsNotFoundThenReturnImageErrorResponse() throws Exception {
        given(imageController.getImageDetails("not-found.jpeg"))
                .willThrow(new ImageRepositoryException());

        final MvcResult mvcResult = mockMvc.perform(get("/photobin/get/image/info/not-found.jpeg"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Is.is("file name not found")))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void givenImageThenReturnImage() throws Exception {
        final ResponseEntity<byte[]> responseEntity = ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(ImageUtility.decompressImage(image.getPhoto()));

        given(imageController.getImage("sky.jpeg")).willReturn(responseEntity);

        final MvcResult mvcResult = mockMvc.perform(get("/photobin/get/image/" + image.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(ImageUtility.decompressImage(image.getPhoto())))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void givenImageNotFoundThenReturnImageErrorResponse() throws Exception {
        given(imageController.getImage("not-found.jpeg"))
                .willThrow(new ImageRepositoryException());

        final MvcResult mvcResult = mockMvc.perform(get("/photobin/get/image/not-found.jpeg"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Is.is("file name not found")))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void givenImageByNameDeleteImageAndReturn() throws Exception {
        final ResponseEntity<ImageResponse> responseEntity = ResponseEntity
                .status(HttpStatus.OK)
                .body(new ImageResponse("Image " + image.getName() + " deleted successfully"));

        given(imageController.deleteImage("sky.jpeg")).willReturn(responseEntity);

        final MvcResult mvcResult = mockMvc.perform(delete("/photobin/delete/image/" + image.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.response",
                        Is.is("Image " + image.getName() + " deleted successfully")))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void givenImageByNameNotFoundReturnImageErrorResponse() throws Exception {
        given(imageController.deleteImage("not-found.jpeg"))
                .willThrow(new ImageRepositoryException());

        final MvcResult mvcResult = mockMvc.perform(delete("/photobin/delete/image/not-found.jpeg"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Is.is("file name not found")))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    private static String generateRandomString(int length) {
        return new SecureRandom().ints(length, 33, 127)
                .mapToObj(i -> (char) i)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

}
