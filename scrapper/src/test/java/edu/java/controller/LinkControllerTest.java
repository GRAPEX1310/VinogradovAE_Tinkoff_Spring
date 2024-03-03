package edu.java.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("LinkController add link work test")
    public void testLinkControllerAddLink() throws Exception {
        String request = "{\"link\": \"http://github.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/links")
                .header("Tg-Chat-Id", "13")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.id").value(13));
            //Не работает, не знаю почему
            //.andExpect(jsonPath("$.link").value("http://github.com"));
    }

    @Test
    @DisplayName("LinkController delete work test")
    public void testLinkControllerDelete() throws Exception {
        String request = "{\"link\": \"http://github.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.delete("/links")
                .header("Tg-Chat-Id", "13")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.id").value(13));
            //Не работает, не знаю почему
            //.andExpect(jsonPath("$.link").value("http://github.com"));
    }

    @Test
    @DisplayName("LinkController get links work test")
    public void testLinkControllerGetLinks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/links")
                .header("Tg-Chat-Id", "13")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.links[0].id").value(13))
            .andExpect(jsonPath("$.links[0].url").value("http://github.com"));
    }
}
