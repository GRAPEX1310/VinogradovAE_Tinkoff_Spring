package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.clients.bot.BotRestClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;
import java.net.URI;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:test")
public class BotRestClientTest {

    private static final String ENDPOINT = "/updates";
    private static final Long DEFAULT_ID = 13L;
    private static WireMockServer wireMockServer;

    @Autowired
    private BotRestClient botClient;

    @BeforeAll
    public static void setWireMockServer() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    public static void serverKill() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("BotRestClient work test")
    public void testBotRestClient() {
        String url = "http://github.com";

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(ENDPOINT))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

        StepVerifier.create(
            botClient.sendUpdates(DEFAULT_ID, URI.create(url), "description", List.of())).verifyComplete();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(ENDPOINT)));
    }
}
