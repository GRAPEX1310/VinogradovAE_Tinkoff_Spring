package edu.java.bot.controller;

import edu.java.dto.request.LinkUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
public class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test LinkUpdateController work")
    public void testLinkUpdateController() throws Exception {
        LinkUpdateRequest linkUpdateRequest =
            new LinkUpdateRequest(1L, URI.create("http://github.com"), "description", List.of(1L, 2L, 3L));
        String requestBody = objectMapper.writeValueAsString(linkUpdateRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
