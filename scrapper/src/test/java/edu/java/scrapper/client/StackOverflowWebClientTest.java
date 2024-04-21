package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.clients.stackoverflow.StackOverflowWebClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;
import java.time.OffsetDateTime;

@SpringBootTest
@TestPropertySource(locations = "classpath:test")
@DirtiesContext
public class StackOverflowWebClientTest {

    private static WireMockServer wireMockServer;

    @Autowired
    private StackOverflowWebClient stackOverflowWebClient;

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
    @DisplayName("StackOverflowWebClient work test")
    public void testStackOverflowWebClient() {
        int questionId = 12345;
        String responseBody = "{\"items\": [{\"question_id\": 12345, \"last_activity_date\": 1687479446}]}";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/questions/" + questionId + "?site=stackoverflow"))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody)));

        StepVerifier.create(stackOverflowWebClient.getQuestionData(questionId))
            .expectNextMatches(response -> {
                OffsetDateTime expectedDate = OffsetDateTime.parse("2023-06-23T00:17:26Z");
                return response.isPresent() && response.get().questionId() == questionId &&
                    response.get().getLastActivityDate().isEqual(expectedDate);
            })
            .verifyComplete();
    }
}
