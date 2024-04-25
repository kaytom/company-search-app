package com.lexis.nexis.poc.controller;

import com.lexis.nexis.poc.model.CompanyLookupRequest;
import com.lexis.nexis.poc.model.CompanySearchResult;
import com.lexis.nexis.poc.service.CompanyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompanySearchController {
    private static final Logger logger = LogManager.getLogger(CompanySearchController.class);

    @Autowired
    private CompanyService companyService;

    @GetMapping("/search")
    public ResponseEntity<?> companyLookup(
            @RequestBody CompanyLookupRequest companyLookupRequest,
            @RequestParam(value = "activeOnly", defaultValue = "true") boolean activeOnly,
            @RequestHeader(value = "x-api-key") String apiKey
    ) {
        logger.info("Starting search request processing");

        String companyName = companyLookupRequest.companyName();
        String companyNumber = companyLookupRequest.companyNumber();

        if (companyName == null && companyNumber == null) {
            return ResponseEntity.badRequest().body("Either companyName or companyNumber must be provided.");
        }

        try {
            CompanySearchResult companySearchResult = companyService.lookupCompanyAndOfficers(companyLookupRequest, activeOnly, apiKey);
            logger.info("Completed search request processing");

            return ResponseEntity.ok(companySearchResult);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }

    }
}
