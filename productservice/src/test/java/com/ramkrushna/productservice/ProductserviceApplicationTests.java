package com.ramkrushna.productservice;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester.MockMvcRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramkrushna.productservice.dto.ProductRequest;
import com.ramkrushna.productservice.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
class ProductserviceApplicationTests {

    @Container
    static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:4.4.2")
            .withExposedPorts(27017);

    static {
        mongoDbContainer.start(); // force start
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dyn) {
        dyn.add("spring.data.mongodb.uri", mongoDbContainer::getReplicaSetUrl);
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(status().isCreated());
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("Iphone 13")
                .description("iphone new model")
                .price(BigDecimal.valueOf(1300))
                .build();
    }
}


