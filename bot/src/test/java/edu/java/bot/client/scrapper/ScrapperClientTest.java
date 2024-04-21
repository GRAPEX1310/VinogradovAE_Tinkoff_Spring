package edu.java.bot.client.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.bot.clients.scrapper.ScrapperClient;
import edu.java.bot.clients.scrapper.exeption.ClientException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;
import java.net.URI;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:test")
@DirtiesContext
public class ScrapperClientTest {

    private static final String CHAT_PREFIX = "/tg-chat/";
    private static final String CHAT_HEADER = "Tg-Chat-Id";
    private static final String LINKS_ENDPOINT = "/links";
    private static final Long DEFAULT_ID = 13L;

    private static WireMockServer wireMockServer;

    @Autowired
    private ScrapperClient scrapperClient;

    @BeforeAll
    public static void setWireMockServer() {
        wireMockServer = new WireMockServer(8088);
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    public static void serverKill() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("ScrapperClient good adding chat")
    public void testScrapperClientGoodAddChat() {
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(CHAT_PREFIX + DEFAULT_ID))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
        StepVerifier.create(scrapperClient.registerChat(DEFAULT_ID)).verifyComplete();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(CHAT_PREFIX + DEFAULT_ID)));

    }

    @Test
    @DisplayName("ScrapperClient double adding chat")
    public void testScrapperClientDoubleAddChat() {
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(CHAT_PREFIX + DEFAULT_ID))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.CONFLICT.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\": \"Conflict\", \"code\": \"409\"}")));

        StepVerifier.create(scrapperClient.registerChat(DEFAULT_ID))
            .expectErrorMatches(throwable -> throwable instanceof ClientException &&
                ((ClientException) throwable).getErrorResponse().description().equals("Conflict") &&
                ((ClientException) throwable).getErrorResponse().code().equals("409"))
            .verify();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(CHAT_PREFIX + DEFAULT_ID)));

    }

    @Test
    @DisplayName("ScrapperClient good deleting chat")
    public void testScrapperClientGoodDeleteChat() {
        WireMock.stubFor(WireMock.delete(WireMock.urlEqualTo(CHAT_PREFIX + DEFAULT_ID))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
        StepVerifier.create(scrapperClient.deleteChat(DEFAULT_ID)).verifyComplete();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(CHAT_PREFIX + DEFAULT_ID)));

    }

    @Test
    @DisplayName("ScrapperClient deleting empty chat")
    public void testScrapperClientDeleteEmptyChat() {
        WireMock.stubFor(WireMock.delete(WireMock.urlEqualTo(CHAT_PREFIX + DEFAULT_ID))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.NOT_ACCEPTABLE.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(
                    "{\"description\": \"Not Acceptable\", \"code\": \"406\", \"exceptionName\": \"ChatIdNotFoundException\", \"exceptionMessage\": \"Chat id for user not found\"}")));

        StepVerifier.create(scrapperClient.deleteChat(DEFAULT_ID))
            .expectErrorMatches(throwable -> throwable instanceof ClientException &&
                ((ClientException) throwable).getErrorResponse().description().equals("Not Acceptable") &&
                ((ClientException) throwable).getErrorResponse().code().equals("406") &&
                ((ClientException) throwable).getErrorResponse().exceptionName().equals("ChatIdNotFoundException") &&
                ((ClientException) throwable).getErrorResponse().exceptionMessage()
                    .equals("Chat id for user not found"))
            .verify();

        WireMock.verify(WireMock.deleteRequestedFor(WireMock.urlEqualTo(CHAT_PREFIX + DEFAULT_ID)));

    }

    @Test
    @DisplayName("ScrapperClient good add Link")
    public void testScrapperClientGoodAddLink() {
        URI link = URI.create("https://github.com");
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(CHAT_HEADER, WireMock.equalTo(DEFAULT_ID.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://github.com\"}"))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"id\": 1, \"url\": \"https://github.com\"}")));

        StepVerifier.create(scrapperClient.addLink(DEFAULT_ID, link))
            .expectNextMatches(linkResponse -> {
                assertThat(linkResponse.id()).isEqualTo(1);
                assertThat(linkResponse.url().toString()).isEqualTo("https://github.com");
                return true;
            }).verifyComplete();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(CHAT_HEADER, WireMock.equalTo(DEFAULT_ID.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://github.com\"}")));

    }

    @Test
    @DisplayName("ScrapperClient double add Link")
    public void testScrapperClientDoubleAddLink() {
        URI link = URI.create("https://github.com");
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(CHAT_HEADER, WireMock.equalTo(DEFAULT_ID.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://github.com\"}"))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\": \"Bad Request\", \"code\": \"400\"}")));

        StepVerifier.create(scrapperClient.addLink(DEFAULT_ID, link))
            .expectErrorMatches(throwable -> throwable instanceof ClientException &&
                ((ClientException) throwable).getErrorResponse().description().equals("Bad Request") &&
                ((ClientException) throwable).getErrorResponse().code().equals("400"))
            .verify();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(CHAT_HEADER, WireMock.equalTo(DEFAULT_ID.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://github.com\"}")));

    }

    @Test
    @DisplayName("ScrapperClient good delete Link")
    public void testScrapperClientGoodDeleteLink() {
        URI link = URI.create("https://github.com");
        WireMock.stubFor(WireMock.delete(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(CHAT_HEADER, WireMock.equalTo(DEFAULT_ID.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://github.com\"}"))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"id\": 1, \"url\": \"https://github.com\"}")));

        StepVerifier.create(scrapperClient.deleteLink(DEFAULT_ID, link))
            .expectNextMatches(linkResponse -> {
                assertThat(linkResponse.id()).isEqualTo(1);
                assertThat(linkResponse.url().toString()).isEqualTo("https://github.com");
                return true;
            }).verifyComplete();

        WireMock.verify(WireMock.deleteRequestedFor(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(CHAT_HEADER, WireMock.equalTo(DEFAULT_ID.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://github.com\"}")));

    }

    @Test
    @DisplayName("ScrapperClient delete empty Link")
    public void testScrapperClientDeleteEmptyLink() {
        URI link = URI.create("https://github.com");
        WireMock.stubFor(WireMock.delete(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(CHAT_HEADER, WireMock.equalTo(DEFAULT_ID.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://github.com\"}"))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\": \"Not Found\", \"code\": \"404\"}")));

        StepVerifier.create(scrapperClient.deleteLink(DEFAULT_ID, link))
            .expectErrorMatches(throwable -> throwable instanceof ClientException &&
                ((ClientException) throwable).getErrorResponse().description().equals("Not Found") &&
                ((ClientException) throwable).getErrorResponse().code().equals("404"))
            .verify();

        WireMock.verify(WireMock.deleteRequestedFor(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(CHAT_HEADER, WireMock.equalTo(DEFAULT_ID.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://github.com\"}")));

    }

    @Test
    @DisplayName("ScrapperClient good get Links list")
    public void testScrapperClientGoodGetLinksList() {
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(CHAT_HEADER, WireMock.equalTo(DEFAULT_ID.toString()))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"links\": [{\"id\": 1, \"url\": \"https://github.com\"}], \"size\": 1}")));

        StepVerifier.create(scrapperClient.getLinksList(DEFAULT_ID))
            .expectNextMatches(linkResponse -> {
                assertThat(linkResponse.links()).hasSize(1);
                assertThat(linkResponse.links().getFirst().url().toString()).isEqualTo("https://github.com");
                return true;
            }).verifyComplete();

        WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(CHAT_HEADER, WireMock.equalTo(DEFAULT_ID.toString())));
    }

    @Test
    @DisplayName("ScrapperClient wrong getting Links list")
    public void testScrapperClientWrongGettingLinksList() {
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(CHAT_HEADER, WireMock.equalTo(DEFAULT_ID.toString()))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\": \"Bad request\", \"code\": \"400\"}")));

        StepVerifier.create(scrapperClient.getLinksList(DEFAULT_ID))
            .expectErrorMatches(throwable -> throwable instanceof ClientException &&
                ((ClientException) throwable).getErrorResponse().description().equals("Bad request") &&
                ((ClientException) throwable).getErrorResponse().code().equals("400"))
            .verify();

        WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(CHAT_HEADER, WireMock.equalTo(DEFAULT_ID.toString())));

    }
}
