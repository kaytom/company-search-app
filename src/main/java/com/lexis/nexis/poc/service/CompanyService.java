package com.lexis.nexis.poc.service;

import com.lexis.nexis.poc.model.CompanyLookupRequest;
import com.lexis.nexis.poc.model.CompanySearchResult;

public interface CompanyService {
    CompanySearchResult lookupCompanyAndOfficers(CompanyLookupRequest companyLookupRequest, boolean activeOnly, String apiKey);
}
