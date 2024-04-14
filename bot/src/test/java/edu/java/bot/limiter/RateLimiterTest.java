package edu.java.bot.limiter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.request.LinkUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.net.URI;
import java.util.List;

@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
public class RateLimiterTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${bucket4j.filters[0].rate-limits[0].bandwidths[0].capacity}")
    private Integer rateLimit;

    @Test
    @DisplayName("Rate limit test")
    public void testRateLimiter() throws Exception {
        final LinkUpdateRequest linkUpdateRequest =
            new LinkUpdateRequest(1L, URI.create("https://aboba.com"), "update", List.of());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(linkUpdateRequest));

        for (int i = 0; i < rateLimit; i++) {
            mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isTooManyRequests());
    }
}
