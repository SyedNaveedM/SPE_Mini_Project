
package com.example.mini_project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAddNumbersOverHttp() throws Exception {
        mockMvc.perform(get("/api/v1/add")
                        .param("a", "10")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.operation").value("add"))
                .andExpect(jsonPath("$.a").value(10))
                .andExpect(jsonPath("$.b").value(5))
                .andExpect(jsonPath("$.result").value(15));
    }

    @Test
    void shouldSubtractNumbersOverHttp() throws Exception {
        mockMvc.perform(get("/api/v1/subtract")
                        .param("a", "10")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("subtract"))
                .andExpect(jsonPath("$.a").value(10))
                .andExpect(jsonPath("$.b").value(5))
                .andExpect(jsonPath("$.result").value(5));
    }

    @Test
    void shouldMultiplyNumbersOverHttp() throws Exception {
        mockMvc.perform(get("/api/v1/multiply")
                        .param("a", "10")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("multiply"))
                .andExpect(jsonPath("$.a").value(10))
                .andExpect(jsonPath("$.b").value(5))
                .andExpect(jsonPath("$.result").value(50));
    }

    @Test
    void shouldDivideNumbersOverHttp() throws Exception {
        mockMvc.perform(get("/api/v1/divide")
                        .param("a", "10")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("divide"))
                .andExpect(jsonPath("$.a").value(10))
                .andExpect(jsonPath("$.b").value(5))
                .andExpect(jsonPath("$.result").value(2));
    }

    @Test
    void shouldReturn400WhenDividingByZero() throws Exception {
        mockMvc.perform(get("/api/v1/divide")
                        .param("a", "10")
                        .param("b", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("Cannot divide by zero"))
                .andExpect(jsonPath("$.error")
                        .value("Bad Request"));
    }

    @Test
    void shouldReturn400WhenParameterIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/add")
                        .param("a", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Missing required parameter: b"))
                .andExpect(jsonPath("$.error")
                        .value("Bad Request"));
    }
}
