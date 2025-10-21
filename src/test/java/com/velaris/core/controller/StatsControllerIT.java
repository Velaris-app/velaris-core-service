package com.velaris.core.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
class StatsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssetRepository assetRepository;

    @BeforeEach
    void setUp() {
        assetRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user = userRepository.save(TestObjects.user(null, "test"));

        AssetEntity asset1 = TestObjects.asset(user.getId(), "Cards", "Card 1", new BigDecimal("10.0"), 2);
        AssetEntity asset2 = TestObjects.asset(user.getId(), "Cards", "Card 2", new BigDecimal("20.0"), 1);

        assetRepository.saveAll(List.of(asset1, asset2));
    }

    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "securityUserService", setupBefore = TEST_EXECUTION)
    void testGetStatsByCategory() throws Exception {
        mockMvc.perform(get("/stats/category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Cards"))
                .andExpect(jsonPath("$[0].totalValue").value(40.0))
                .andExpect(jsonPath("$[0].itemCount").value(3));
    }

    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "securityUserService", setupBefore = TEST_EXECUTION)
    void testGetStatsOverview() throws Exception {
        mockMvc.perform(get("/stats/overview")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalValue").value(40.0))
                .andExpect(jsonPath("$.totalItems").value(3))
                .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "securityUserService", setupBefore = TEST_EXECUTION)
    void testGetStatsTrend() throws Exception {
        mockMvc.perform(get("/stats/trend")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalValue").exists());
    }
}
