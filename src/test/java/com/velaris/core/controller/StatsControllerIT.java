package com.velaris.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velaris.api.model.TrendRequest;
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
import java.time.OffsetDateTime;
import java.util.List;

import static org.springframework.security.test.context.support.TestExecutionEvent.TEST_EXECUTION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        assetRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user = userRepository.save(TestObjects.user(null, "test"));

        AssetEntity asset1 = TestObjects.asset(user, "Cards", "Card 1", new BigDecimal("10.0"), 2);
        AssetEntity asset2 = TestObjects.asset(user, "Cards", "Card 2", new BigDecimal("20.0"), 1);

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
                .andExpect(jsonPath("$[0].uniqueAssets").value(2))
                .andExpect(jsonPath("$[0].itemCount").value(3))
                .andExpect(jsonPath("$[0].percentageOfTotal").value(1.0));
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
        TrendRequest request = new TrendRequest()
                .startDate(OffsetDateTime.now().minusDays(3))
                .endDate(OffsetDateTime.now());

        mockMvc.perform(post("/stats/trend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalValue").exists())
                .andExpect(jsonPath("$[0].date").exists())
                .andExpect(jsonPath("$[0].itemsAdded").exists());
    }
}
