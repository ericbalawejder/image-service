package com.service.image;

import com.service.image.controller.ImageController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ImageServiceApplicationTests {

    @Autowired
    private ImageController imageController;

    @Test
    void contextLoads() {
        assertThat(imageController).isNotNull();
    }

}
