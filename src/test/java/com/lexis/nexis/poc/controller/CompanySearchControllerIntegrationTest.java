package com.lexis.nexis.poc.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.lexis.nexis.poc.model.CompanyLookupRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import wiremock.com.fasterxml.jackson.core.JsonProcessingException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CompanySearchControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    @RegisterExtension
    static WireMockExtension wireMockServer =
            WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("truProxyApi.baseUrl", wireMockServer::baseUrl);
    }

    @AfterEach
    void resetAll() {
        wireMockServer.resetAll();
    }

    @Test
    void basicWireMockExample() throws JsonProcessingException {

        wireMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo("/TruProxyAPI/rest/Companies/v1/Search?Query=06500244"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("external-api/companies-response-200.json")));
        wireMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo("/TruProxyAPI/rest/Companies/v1/Officers?CompanyNumber=06500244"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("external-api/officers-response-200.json")));

        CompanyLookupRequest request = new CompanyLookupRequest("BBC Limited", "06500244");

        this.webTestClient
                .method(HttpMethod.GET)
                .uri("/search?activeOnly=true")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-api-key", "PwewCEztSW7XlaAKqkg4IaOsPelGynw6SN9WsbNf")
                .body(Mono.just(request), CompanyLookupRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.length()")
                .isEqualTo(2)
                .jsonPath("$.total_results")
                .isEqualTo(1)
                .jsonPath("$.items.length()")
                .isEqualTo(1)
                .jsonPath("$.items[0].company_number")
                .isEqualTo("06500244")
                .jsonPath("$.items[0].company_type")
                .isEqualTo("ltd")
                .jsonPath("$.items[0].title")
                .isEqualTo("BBC LIMITED")
                .jsonPath("$.items[0].company_status")
                .isEqualTo("active")
                .jsonPath("$.items[0].date_of_creation")
                .isEqualTo("2008-02-11")
                .jsonPath("$.items[0].address.length()")
                .isEqualTo(5);
    }
}
