package com.lexis.nexis.poc.service;

import com.lexis.nexis.poc.exception.NoMatchingCompanyException;
import com.lexis.nexis.poc.exception.NoMatchingEmployeeException;
import com.lexis.nexis.poc.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
public class CompanyServiceImpl implements CompanyService {
    private static final Logger logger = LogManager.getLogger(CompanyServiceImpl.class);
    private final WebClient webClient;

    public CompanyServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public CompanySearchResult lookupCompanyAndOfficers(CompanyLookupRequest companyLookupRequest, boolean activeOnly, String apiKey) {

        logger.info("Started company and officers lookup");

        CompanyResponse companiesListResponse = getCompaniesList(apiKey, companyLookupRequest);

        validateCompanySearchResult(companiesListResponse);

        List<Company> companiesWithAssociatedOfficersList = composeCompaniesWithOfficers(activeOnly, apiKey, companiesListResponse);

        CompanySearchResult companySearchResult = new CompanySearchResult(companiesListResponse.total_results(), companiesWithAssociatedOfficersList);

        //TODO to highlight a potential integration point for database operation
        if(isCompanySearchResultValid(companySearchResult)){
            saveCompanyAndOfficers(companySearchResult);
        }

        logger.info("Completed company and officers lookup");

        return companySearchResult;
    }

    private CompanyResponse getCompaniesList(String apiKey, CompanyLookupRequest companyLookupRequest) {
        String urlCompanySearch = ApiUriConstants.TRU_PROXY_API_BASE_URL.concat(ApiUriConstants.TRU_PROXY_COMPANY_SEARCH_URL);
        UriComponentsBuilder companySearchBuilder = UriComponentsBuilder.fromHttpUrl(urlCompanySearch);

        if (companyLookupRequest.companyNumber() != null) {
            companySearchBuilder.queryParam(ApiUriConstants.QUERY_API_PARAM_KEY, companyLookupRequest.companyNumber());
        } else {
            companySearchBuilder.queryParam(ApiUriConstants.QUERY_API_PARAM_KEY, companyLookupRequest.companyName());
        }

        return webClient
                .get()
                .uri(companySearchBuilder.toUriString())
                .header(ApiUriConstants.X_API_KEY, apiKey)
                .retrieve()
                .bodyToMono(CompanyResponse.class)
                .block();
    }

    private static void validateCompanySearchResult(CompanyResponse companiesListResponse) {
        if (companiesListResponse != null && companiesListResponse.items() == null) {
            throw new NoMatchingCompanyException("The search criteria did not match any company");
        }
    }

    private List<Company> composeCompaniesWithOfficers(boolean activeOnly, String apiKey, CompanyResponse companiesListResponse) {

        List<Company> companiesWithAssociatedOfficersList = new ArrayList<>();

        Predicate<CompanyResponseItem> isCompanyActive = c -> c.company_status().equals(ApiUriConstants.ACTIVE_COMPANY_STATUS);
        Predicate<CompanyResponseItem> allCompaniesWithStatus = c -> !c.company_status().isBlank();
        final Predicate<CompanyResponseItem> isActivePredicate =
                activeOnly ? isCompanyActive : allCompaniesWithStatus;

        companiesListResponse.items()
                .stream()
                .filter(isActivePredicate)
                .forEach(company -> {
                    String officerSearchurl = ApiUriConstants.TRU_PROXY_API_BASE_URL.concat(ApiUriConstants.TRU_PROXY_OFFICERS_SEARCH_URL);
                    UriComponentsBuilder officerSearchBuilder = UriComponentsBuilder.fromHttpUrl(officerSearchurl);
                    officerSearchBuilder.queryParam(ApiUriConstants.COMPANY_NUMBER_API_PARAM_KEY, company.company_number());

                    List<OfficerView> officerList = getCompanyOfficersList(officerSearchBuilder, apiKey);

                    companiesWithAssociatedOfficersList.add(new Company(
                                    company.company_number(),
                                    company.company_type(),
                                    company.title(),
                                    company.company_status(),
                                    company.date_of_creation(),
                                    company.address(),
                                    officerList
                            )
                    );
                });
        return companiesWithAssociatedOfficersList;
    }

    private List<OfficerView> getCompanyOfficersList(UriComponentsBuilder officerSearchBuilder, String apiKey) {

        CompanyOfficersResponse companyResponse = webClient
                .get()
                .uri(officerSearchBuilder.toUriString())
                .header(ApiUriConstants.X_API_KEY, apiKey)
                .retrieve()
                .bodyToMono(CompanyOfficersResponse.class)
                .block();

        if (companyResponse != null && companyResponse.items() == null) {
            throw new NoMatchingEmployeeException("The search criteria did not match any employees");
        }

        List<OfficerView> officerViewList = new ArrayList<>();

        assert companyResponse != null;
        companyResponse.items()
                .stream()
                .filter(officer -> officer.resigned_on() == null)
                .forEach(officer -> officerViewList.add(new OfficerView(officer.name(),
                        officer.officer_role(),
                        officer.appointed_on(),
                        new Address(officer.address().locality(),
                                officer.address().postal_code(),
                                officer.address().premises(),
                                officer.address().address_line_1(),
                                officer.address().country())
                )));
        return officerViewList;
    }

    private boolean isCompanySearchResultValid(CompanySearchResult companySearchResult){
        //TODO: Validation of company search result data before executing database operation
        return true;
    }

    private void saveCompanyAndOfficers(CompanySearchResult companySearchResult){
        //TODO: Saves the company and associated officers to the database
    }

}
