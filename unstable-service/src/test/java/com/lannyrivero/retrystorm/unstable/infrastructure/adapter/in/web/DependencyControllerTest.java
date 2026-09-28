package com.lannyrivero.retrystorm.unstable.infrastructure.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class DependencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsOkWhenDependencyCallSucceeds() throws Exception {
        mockMvc.perform(get("/api/dependency")
                        .param("seed", "481516")
                        .param("logicalRequestId", "1")
                        .param("attempt", "1")
                        .param("failureRate", "0")
                        .param("latencyMs", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.logicalRequestId").value(1))
                .andExpect(jsonPath("$.attempt").value(1))
                .andExpect(jsonPath("$.successful").value(true));
    }

    @Test
    void returnsServiceUnavailableWhenDependencyCallFails() throws Exception {
        mockMvc.perform(get("/api/dependency")
                        .param("seed", "481516")
                        .param("logicalRequestId", "1")
                        .param("attempt", "1")
                        .param("failureRate", "1")
                        .param("latencyMs", "0"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.logicalRequestId").value(1))
                .andExpect(jsonPath("$.attempt").value(1))
                .andExpect(jsonPath("$.successful").value(false));
    }

    @Test
    void returnsBadRequestWhenRequestIsInvalid() throws Exception {
        mockMvc.perform(get("/api/dependency")
                        .param("seed", "481516")
                        .param("logicalRequestId", "0")
                        .param("attempt", "1")
                        .param("failureRate", "0.3")
                        .param("latencyMs", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("logicalRequestId must be positive"));
    }
}
