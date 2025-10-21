package com.velaris.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velaris.api.model.Asset;
import com.velaris.core.IntegrationTest;
import com.velaris.core.TestObjects;
import com.velaris.core.entity.AssetEntity;
import com.velaris.core.entity.UserEntity;
import com.velaris.core.repository.AssetRepository;
import com.velaris.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.security.test.context.support.TestExecutionEvent.TEST_EXECUTION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
class AssetsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssetRepository assetRepository;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        assetRepository.deleteAll();
        userRepository.deleteAll();

        testUser = userRepository.save(TestObjects.user(null, "test"));

        AssetEntity asset1 = TestObjects.asset(testUser.getId(), "Cards", "Card 1", new BigDecimal("10.0"), 2);
        AssetEntity asset2 = TestObjects.asset(testUser.getId(), "Cards", "Card 2", new BigDecimal("20.0"), 1);

        assetRepository.saveAll(List.of(asset1, asset2));
    }

    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "securityUserService", setupBefore = TEST_EXECUTION)
    void testAddAsset() throws Exception {
        Asset asset = new Asset();
        asset.setName("New Asset");
        asset.setPurchasePrice(BigDecimal.valueOf(50.0));
        asset.setQuantity(3);

        mockMvc.perform(post("/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(asset)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("New Asset"))
                .andExpect(jsonPath("$.purchasePrice").value(50.0))
                .andExpect(jsonPath("$.quantity").value(3));
    }

    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "securityUserService", setupBefore = TEST_EXECUTION)
    void testGetAsset() throws Exception {
        AssetEntity existingAsset = assetRepository.findAll().stream().findFirst().orElseThrow();

        mockMvc.perform(get("/assets/" + existingAsset.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingAsset.getId()))
                .andExpect(jsonPath("$.name").value(existingAsset.getName()));
    }

    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "securityUserService", setupBefore = TEST_EXECUTION)
    void testListAssets() throws Exception {
        mockMvc.perform(get("/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Card 1"))
                .andExpect(jsonPath("$[1].name").value("Card 2"));
    }

    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "securityUserService", setupBefore = TEST_EXECUTION)
    void testModifyAsset() throws Exception {
        AssetEntity existingAsset = assetRepository.findAll().stream().findFirst().orElseThrow();

        Asset assetUpdate = new Asset();
        assetUpdate.setName("Updated Name");
        assetUpdate.setPurchasePrice(BigDecimal.valueOf(100.0));
        assetUpdate.setQuantity(5);

        mockMvc.perform(put("/assets/" + existingAsset.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assetUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingAsset.getId()))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.purchasePrice").value(100.0))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "securityUserService", setupBefore = TEST_EXECUTION)
    void testDeleteAsset() throws Exception {
        AssetEntity existingAsset = assetRepository.findAll().stream().findFirst().orElseThrow();

        mockMvc.perform(delete("/assets/" + existingAsset.getId()))
                .andExpect(status().isNoContent());
    }
}