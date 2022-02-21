package com.service.image.controller;

import com.service.image.entities.Image;
import com.service.image.repositories.ImageRepository;
import com.service.image.util.ImageUtility;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @MockBean
    private ImageRepository imageRepository;

    @Autowired
    private MockMvc mockMvc;

    private Image image;

    @BeforeEach
    void setUp() {
        image = new Image(100_000_000_000L,
                "sunset.jpeg",
                "jpeg",
                900_000_000_000L,
                "hash",
                ImageUtility.compressImage(new byte[]{1, 2, 3, 4}));
    }

    @Test
    public void givenImageGetImageDetails() throws Exception {
        given(imageRepository.findByName("sunset.jpeg")).willReturn(Optional.ofNullable(image));

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
