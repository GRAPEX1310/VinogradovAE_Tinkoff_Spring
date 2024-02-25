package edu.java.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.clients.GitHubWebClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@TestPropertySource(locations = "classpath:test")
public class GitHubClientTest {

    private static WireMockServer wireMockServer;

    @Autowired
    private GitHubWebClient gitHubWebClient;

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
    @DisplayName("GitHubWebClient work test")
    public void testGitHubClient() {
        String ownerName = "testOwner";
        String repositoryName = "testRepo";
        String responseBody = "{\"name\":\"testRepo\",\"full_name\":\"https://github.com/testOwner/testRepo\"," +
            "\"html_url\":\"https://github.com/testOwner/testRepo\",\"updated_at\":\"2022-02-01T00:00:00Z\"," +
            "\"pushed_at\":\"2022-03-01T00:00:00Z\"}";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/" + ownerName + "/" + repositoryName))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody)));

        StepVerifier.create(gitHubWebClient.getRepositoryData(ownerName, repositoryName))
            .expectNextMatches(response -> {
                OffsetDateTime expectedDate = OffsetDateTime.parse("2022-02-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                return response.fullName().equals("https://github.com/testOwner/testRepo") &&
                    response.updatedAt().isEqual(expectedDate);
            })
            .verifyComplete();
    }
}
