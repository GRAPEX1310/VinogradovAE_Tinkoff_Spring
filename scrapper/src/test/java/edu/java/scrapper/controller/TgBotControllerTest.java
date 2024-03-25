package edu.java.scrapper.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
public class TgBotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("TgBotController register work test")
    public void testTgBotControllerRegister() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tg-bot/13")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("TgBotController delete work test")
    public void testTgBotControllerDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tg-bot/13")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
