package edu.java.scrapper.limiter;

import edu.java.dto.response.LinkListResponse;
import edu.java.scrapper.service.LinkService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
public class RateLimiterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LinkService linkService;

    @Value("${bucket4j.filters[0].rate-limits[0].bandwidths[0].capacity}")
    private Integer rateLimit;

    @Test
    @DisplayName("Test rate limiter work")
    public void testRateLimiter() throws Exception {
        final Long id = 1L;
        Mockito.doReturn(List.of()).when(linkService).findAllLinksForUser(id);
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/links")
            .header("Tg-Chat-Id", id);

        for (int i = 0; i < rateLimit; i++) {
            mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isTooManyRequests());
    }
}
