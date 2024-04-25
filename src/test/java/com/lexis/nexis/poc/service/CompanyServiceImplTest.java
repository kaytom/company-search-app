package com.lexis.nexis.poc.service;

import com.lexis.nexis.poc.model.CompanyLookupRequest;
import com.lexis.nexis.poc.model.CompanySearchResult;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CompanyServiceImplTest {
    public static MockWebServer mockBackEnd;

    @Autowired
    private CompanyService companyService;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    public void testLookupCompanyAndOfficers_ValidRequest_InvalidApiKey_ThrowsException() {
        boolean activeOnly = true;
        String apiKey = "invalid-api-key";
        CompanyLookupRequest mockRequest = new CompanyLookupRequest("BBC Limited", "06500244");

        AtomicReference<CompanySearchResult> companySearchResult = null;

        Exception exception = assertThrows(WebClientResponseException.class, () -> companySearchResult.set(companyService.lookupCompanyAndOfficers(
                mockRequest,
                activeOnly,
                apiKey)));

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("403 Forbidden from GET"));
    }

    @Disabled("Specify the real api keys in the test method to run")
    @Test
    public void testLookupCompanyAndOfficers_ValidRequest_ReturnsCompanySearchResult() {
        boolean activeOnly = true;
        String apiKey = "valid-api-key-placeholder";
        CompanyLookupRequest mockRequest = new CompanyLookupRequest("BBC Limited", "06500244");

        CompanySearchResult companySearchResult = companyService.lookupCompanyAndOfficers(
                mockRequest,
                activeOnly,
                apiKey);

        Assertions.assertEquals(companySearchResult.total_results(), 1);
        Assertions.assertEquals(companySearchResult.items().size(), 1);
        Assertions.assertEquals(companySearchResult.items().get(0).company_status(), "active");
        Assertions.assertEquals(companySearchResult.items().get(0).company_number(), "06500244");
        Assertions.assertEquals(companySearchResult.items().get(0).company_type(), "ltd");
        Assertions.assertEquals(companySearchResult.items().get(0).date_of_creation(), "2008-02-11");
        Assertions.assertEquals(companySearchResult.items().get(0).title(), "BBC LIMITED");
    }

}
