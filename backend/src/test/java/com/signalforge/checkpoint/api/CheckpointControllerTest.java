package com.signalforge.checkpoint.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CheckpointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnSeededCheckpoints() throws Exception {
        mockMvc.perform(get("/api/checkpoints"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].serviceName").exists())
                .andExpect(jsonPath("$[0].assessment.posture").exists());
    }

    @Test
    void shouldCreateCheckpoint() throws Exception {
        String payload = """
                {
                  "serviceName": "orders-service",
                  "owner": "Fulfillment",
                  "environment": "production",
                  "releaseWindow": "2026-04-25T18:00:00Z",
                  "rolloutStrategy": "CANARY",
                  "changeSize": "MEDIUM",
                  "testPassRate": 96,
                  "incidentCount": 1,
                  "errorBudgetRemaining": 68,
                  "infraHealth": 87,
                  "rollbackReady": true,
                  "notes": "Warehouse routing refinement."
                }
                """;

        mockMvc.perform(post("/api/checkpoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serviceName").value("orders-service"))
                .andExpect(jsonPath("$.assessment.riskScore").exists())
                .andExpect(jsonPath("$.assessment.recommendedActions").isArray());
    }
}
