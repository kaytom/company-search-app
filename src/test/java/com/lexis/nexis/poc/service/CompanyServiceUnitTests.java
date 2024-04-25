package com.lexis.nexis.poc.service;

import com.lexis.nexis.poc.model.CompanyLookupRequest;
import com.lexis.nexis.poc.model.CompanySearchResult;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompanyServiceUnitTests {
    private CompanyService companyService;

    private void buildWebClient(String mockResponse) {
        WebClient webClient = WebClient.builder()
                .exchangeFunction(clientRequest ->
                        Mono.just(ClientResponse.create(HttpStatus.OK)
                                .header("content-type", "application/json")
                                .body(mockResponse)
                                .build())
                ).build();

        companyService = new CompanyServiceImpl(webClient);
    }

    @Test
    public void testLookupCompanyAndOfficers_WhenBothCompanyNumberAndName_ActiveOnlyTrue_ResultsReturned() {
        buildWebClient(buildMockResponseWhenCompanyNameAndNumberIsPresent());
        CompanyLookupRequest mockCompanyLookupRequest = new CompanyLookupRequest("BBC Tech", "123456");

        CompanySearchResult result = companyService.lookupCompanyAndOfficers(mockCompanyLookupRequest, true, "testApiKey");

        assertEquals(result.total_results(),1);
        assertEquals(result.items().size(),1);

    }

    @Test
    public void testLookupCompanyAndOfficers_WhenBothCompanyNumberAndName_ActiveOnlyFalse_ResultsReturned() {
        buildWebClient(buildMockResponseWhenCompanyNameAndNumberIsPresent());
        CompanyLookupRequest mockCompanyLookupRequest = new CompanyLookupRequest("BBC Tech", "123456");

        CompanySearchResult result = companyService.lookupCompanyAndOfficers(mockCompanyLookupRequest, false, "testApiKey");

        assertEquals(result.total_results(),1);
        assertEquals(result.items().size(),1);

    }

    @Test
    public void testLookupCompanyAndOfficers_WhenOnlyCompanyName_ActiveOnlyFalse_ResultsReturned() {
        buildWebClient(buildMockResponseWhenOnlyCompanyNameIsPresent());
        CompanyLookupRequest mockCompanyLookupRequest = new CompanyLookupRequest("BBC Tech", null);

        CompanySearchResult result = companyService.lookupCompanyAndOfficers(mockCompanyLookupRequest, false, "testApiKey");

        assertEquals(result.total_results(),20);
        assertEquals(result.items().size(),16);

    }

    private String buildMockResponseWhenCompanyNameAndNumberIsPresent() {
        return """
                {
                    "total_results": 1,
                    "items": [
                        {
                            "company_number": "06500244",
                            "company_type": "ltd",
                            "title": "BBC LIMITED",
                            "company_status": "active",
                            "date_of_creation": "2008-02-11",
                            "address": {
                                "locality": "Retford",
                                "postal_code": "DN22 0AD",
                                "premises": "Boswell Cottage Main Street",
                                "address_line_1": "North Leverton",
                                "country": "England"
                            },
                            "officers": [
                                {
                                    "name": "BOXALL, Sarah Victoria",
                                    "officer_role": "secretary",
                                    "appointed_on": "2008-02-11",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "SW20 0DP",
                                        "premises": "5",
                                        "address_line_1": "Cranford Close",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "BRAY, Simon Anton",
                                    "officer_role": "director",
                                    "appointed_on": "2008-02-11",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "SW20 0DP",
                                        "premises": "5",
                                        "address_line_1": "Cranford Close",
                                        "country": "England"
                                    }
                                }
                            ]
                        }
                    ]
                }""";
    }

    private String buildMockResponseWhenOnlyCompanyNameIsPresent() {
        return """
                {
                    "total_results": 20,
                    "items": [
                        {
                            "company_number": "06500244",
                            "company_type": "ltd",
                            "title": "BBC LIMITED",
                            "company_status": "active",
                            "date_of_creation": "2008-02-11",
                            "address": {
                                "locality": "Retford",
                                "postal_code": "DN22 0AD",
                                "premises": "Boswell Cottage Main Street",
                                "address_line_1": "North Leverton",
                                "country": "England"
                            },
                            "officers": [
                                {
                                    "name": "BOXALL, Sarah Victoria",
                                    "officer_role": "secretary",
                                    "appointed_on": "2008-02-11",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "SW20 0DP",
                                        "premises": "5",
                                        "address_line_1": "Cranford Close",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "BRAY, Simon Anton",
                                    "officer_role": "director",
                                    "appointed_on": "2008-02-11",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "SW20 0DP",
                                        "premises": "5",
                                        "address_line_1": "Cranford Close",
                                        "country": "England"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "07520089",
                            "company_type": "ltd",
                            "title": "BBC AND CO LIMITED",
                            "company_status": "active",
                            "date_of_creation": "2011-02-07",
                            "address": {
                                "locality": "Lostwithiel",
                                "postal_code": "PL22 0HG",
                                "premises": "Unit 2",
                                "address_line_1": "Restormel Estate",
                                "country": null
                            },
                            "officers": [
                                {
                                    "name": "BATE, Philip Henry",
                                    "officer_role": "director",
                                    "appointed_on": "2011-02-07",
                                    "address": {
                                        "locality": "Lostwithiel",
                                        "postal_code": "PL22 0HG",
                                        "premises": "Unit 2",
                                        "address_line_1": "Restormel Estate",
                                        "country": "United Kingdom"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "09670032",
                            "company_type": "ltd",
                            "title": "BBC AESTHETICS LIMITED",
                            "company_status": "active",
                            "date_of_creation": "2015-07-03",
                            "address": {
                                "locality": "Liverpool",
                                "postal_code": "L3 9NG",
                                "premises": "Suite 4",
                                "address_line_1": "Second Floor Honeycomb",
                                "country": "England"
                            },
                            "officers": [
                                {
                                    "name": "LEWIS, Natasha Patricia",
                                    "officer_role": "director",
                                    "appointed_on": "2015-07-03",
                                    "address": {
                                        "locality": "Liverpool",
                                        "postal_code": "L3 9NG",
                                        "premises": "Suite 4 Second Floor",
                                        "address_line_1": "Honeycomb",
                                        "country": "England"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "07215276",
                            "company_type": "ltd",
                            "title": "BBC APPLICATIONS LIMITED",
                            "company_status": "active",
                            "date_of_creation": "2010-04-07",
                            "address": {
                                "locality": "Bingley",
                                "postal_code": "BD16 2RX",
                                "premises": "2",
                                "address_line_1": "Longwood Hall",
                                "country": "England"
                            },
                            "officers": [
                                {
                                    "name": "HALAY, Howerd",
                                    "officer_role": "secretary",
                                    "appointed_on": "2010-04-07",
                                    "address": {
                                        "locality": "Bingley",
                                        "postal_code": "BD16 2RX",
                                        "premises": "2",
                                        "address_line_1": "Longwood Hall",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "HALAY, Howerd",
                                    "officer_role": "director",
                                    "appointed_on": "2010-04-07",
                                    "address": {
                                        "locality": "Bingley",
                                        "postal_code": "BD16 2RX",
                                        "premises": "2",
                                        "address_line_1": "Longwood Hall",
                                        "country": "United Kingdom"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "SC783452",
                            "company_type": "ltd",
                            "title": "BBC A24 LTD",
                            "company_status": "active",
                            "date_of_creation": "2023-09-21",
                            "address": {
                                "locality": "Edinburgh",
                                "postal_code": "EH2 3JP",
                                "premises": "3f1",
                                "address_line_1": "Third Floor, 3 Hill Street",
                                "country": "Scotland"
                            },
                            "officers": [
                                {
                                    "name": "WESTON, James",
                                    "officer_role": "director",
                                    "appointed_on": "2023-09-21",
                                    "address": {
                                        "locality": "Edinburgh",
                                        "postal_code": "EH2 3JP",
                                        "premises": "3f1 Third Floor, 3",
                                        "address_line_1": "Hill Street",
                                        "country": "Scotland"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "14006528",
                            "company_type": "ltd",
                            "title": "BBC - BLESSING BEAUTY COSMETICS LIMITED",
                            "company_status": "active",
                            "date_of_creation": "2022-03-28",
                            "address": {
                                "locality": "London",
                                "postal_code": "N9 9QR",
                                "premises": "23",
                                "address_line_1": "West Close",
                                "country": "England"
                            },
                            "officers": [
                                {
                                    "name": "ADEBANJO, Blessing Bunmi",
                                    "officer_role": "director",
                                    "appointed_on": "2022-03-28",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "N9 9QR",
                                        "premises": "23",
                                        "address_line_1": "West Close",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "ADEDIBU, Danniella Bolade",
                                    "officer_role": "director",
                                    "appointed_on": "2022-03-28",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "N9 9QR",
                                        "premises": "23",
                                        "address_line_1": "West Close",
                                        "country": "England"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "11811163",
                            "company_type": "ltd",
                            "title": "BBC CARE SERVICE LIMITED",
                            "company_status": "active",
                            "date_of_creation": "2019-02-06",
                            "address": {
                                "locality": "Chertsey",
                                "postal_code": "KT16 0RS",
                                "premises": "3000",
                                "address_line_1": "Hillswood Drive",
                                "country": "England"
                            },
                            "officers": [
                                {
                                    "name": "KABALI, Milley",
                                    "officer_role": "director",
                                    "appointed_on": "2019-02-06",
                                    "address": {
                                        "locality": "Chertsey",
                                        "postal_code": "KT16 0RS",
                                        "premises": "3000",
                                        "address_line_1": "Hillswood Drive",
                                        "country": "England"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "12800068",
                            "company_type": "ltd",
                            "title": "BBC CATERING LTD",
                            "company_status": "active",
                            "date_of_creation": "2020-08-07",
                            "address": {
                                "locality": "Blackburn",
                                "postal_code": "BB2 2AN",
                                "premises": "Unit C",
                                "address_line_1": "Weir St",
                                "country": "United Kingdom"
                            },
                            "officers": [
                                {
                                    "name": "RIAZ, Umair",
                                    "officer_role": "director",
                                    "appointed_on": "2023-07-01",
                                    "address": {
                                        "locality": "Blackburn",
                                        "postal_code": "BB2 2AN",
                                        "premises": "Unit C",
                                        "address_line_1": "Weir St",
                                        "country": "United Kingdom"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "04723022",
                            "company_type": "private-limited-guarant-nsc-limited-exemption",
                            "title": "BBC CHILDREN IN NEED",
                            "company_status": "active",
                            "date_of_creation": "2003-04-04",
                            "address": {
                                "locality": "Mediacity Uk",
                                "postal_code": "M50 2LH",
                                "premises": "4th",
                                "address_line_1": "Floor Dock House",
                                "country": "England"
                            },
                            "officers": [
                                {
                                    "name": "OKOTIE, Anthony",
                                    "officer_role": "secretary",
                                    "appointed_on": "2020-12-01",
                                    "address": {
                                        "locality": "Mediacity Uk",
                                        "postal_code": "M50 2LH",
                                        "premises": "4th Floor Dock House",
                                        "address_line_1": "4th Floor Dock House",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "BHAMRA, Sandeep",
                                    "officer_role": "director",
                                    "appointed_on": "2021-10-14",
                                    "address": {
                                        "locality": "Mediacity Uk",
                                        "postal_code": "M50 2LH",
                                        "premises": "4th Floor Dock House",
                                        "address_line_1": "4th Floor Dock House",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "BIJA, Cherrie Michelle",
                                    "officer_role": "director",
                                    "appointed_on": "2022-05-11",
                                    "address": {
                                        "locality": "Mediacity Uk",
                                        "postal_code": "M50 2LH",
                                        "premises": "4th Floor Dock House",
                                        "address_line_1": "4th Floor Dock House",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "BRADLEY, Trevor Bryan",
                                    "officer_role": "director",
                                    "appointed_on": "2019-06-26",
                                    "address": {
                                        "locality": "Mediacity Uk",
                                        "postal_code": "M50 2LH",
                                        "premises": "4th Floor Dock House",
                                        "address_line_1": "4th Floor Dock House",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "BRYAN, Randel",
                                    "officer_role": "director",
                                    "appointed_on": "2022-01-17",
                                    "address": {
                                        "locality": "Mediacity Uk",
                                        "postal_code": "M50 2LH",
                                        "premises": "4th Floor Dock House",
                                        "address_line_1": "4th Floor Dock House",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "BURNS, Rhona Ann",
                                    "officer_role": "director",
                                    "appointed_on": "2020-05-02",
                                    "address": {
                                        "locality": "Mediacity Uk",
                                        "postal_code": "M50 2LH",
                                        "premises": "4th Floor Dock House",
                                        "address_line_1": "4th Floor Dock House",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "CLIFTON, Kieran Oliver Edward",
                                    "officer_role": "director",
                                    "appointed_on": "2019-10-15",
                                    "address": {
                                        "locality": "Mediacity Uk",
                                        "postal_code": "M50 2LH",
                                        "premises": "4th Floor Dock House",
                                        "address_line_1": "4th Floor Dock House",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "FAIRCLOUGH, James",
                                    "officer_role": "director",
                                    "appointed_on": "2021-10-01",
                                    "address": {
                                        "locality": "Mediacity Uk",
                                        "postal_code": "M50 2LH",
                                        "premises": "4th Floor Dock House",
                                        "address_line_1": "4th Floor Dock House",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "IMAFIDON, Kenny",
                                    "officer_role": "director",
                                    "appointed_on": "2019-04-30",
                                    "address": {
                                        "locality": "Salford",
                                        "postal_code": "M50 2BH",
                                        "premises": "Bridge House",
                                        "address_line_1": "Media City Uk",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "LAMB, Suzanne",
                                    "officer_role": "director",
                                    "appointed_on": "2020-12-01",
                                    "address": {
                                        "locality": "Mediacity Uk",
                                        "postal_code": "M50 2LH",
                                        "premises": "4th Floor Dock House",
                                        "address_line_1": "4th Floor Dock House",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "MILLARD, Rosemary Harriet",
                                    "officer_role": "director",
                                    "appointed_on": "2018-01-17",
                                    "address": {
                                        "locality": "Mediacity Uk",
                                        "postal_code": "M50 2LH",
                                        "premises": "4th Floor Dock House",
                                        "address_line_1": "4th Floor Dock House",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "MUNRO, Jonathan Charles",
                                    "officer_role": "director",
                                    "appointed_on": "2020-05-02",
                                    "address": {
                                        "locality": "Mediacity Uk",
                                        "postal_code": "M50 2LH",
                                        "premises": "4th Floor Dock House",
                                        "address_line_1": "4th Floor Dock House",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "TAVAZIVA, Leigh",
                                    "officer_role": "director",
                                    "appointed_on": "2022-10-01",
                                    "address": {
                                        "locality": "Mediacity Uk",
                                        "postal_code": "M50 2LH",
                                        "premises": "4th Floor Dock House",
                                        "address_line_1": "4th Floor Dock House",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "WALLACE, Jo",
                                    "officer_role": "director",
                                    "appointed_on": "2023-10-19",
                                    "address": {
                                        "locality": "Mediacity Uk",
                                        "postal_code": "M50 2LH",
                                        "premises": "4th Floor Dock House",
                                        "address_line_1": "4th Floor Dock House",
                                        "country": "England"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "09905558",
                            "company_type": "ltd",
                            "title": "BBC CHILDREN'S PRODUCTIONS LIMITED",
                            "company_status": "active",
                            "date_of_creation": "2015-12-07",
                            "address": {
                                "locality": "London",
                                "postal_code": "W12 7FA",
                                "premises": "1",
                                "address_line_1": "Television Centre",
                                "country": "England"
                            },
                            "officers": [
                                {
                                    "name": "RYLAND, Jackline",
                                    "officer_role": "secretary",
                                    "appointed_on": "2023-08-24",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1 Television Centre",
                                        "address_line_1": "101 Wood Lane",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "ESCOTT, Sarah",
                                    "officer_role": "director",
                                    "appointed_on": "2024-03-28",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1 Television Centre",
                                        "address_line_1": "Wood Lane",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "MILLS, Jonathan Charles",
                                    "officer_role": "director",
                                    "appointed_on": "2023-02-01",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1 Television Centre",
                                        "address_line_1": "Wood Lane",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "PERSSON, Cecilia Inger",
                                    "officer_role": "director",
                                    "appointed_on": "2022-04-01",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1 Television Centre",
                                        "address_line_1": "Wood Lane",
                                        "country": "England"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "07211699",
                            "company_type": "private-limited-guarant-nsc",
                            "title": "BBC CLUB SPORTS AND LEISURE LIMITED",
                            "company_status": "active",
                            "date_of_creation": "2010-04-01",
                            "address": {
                                "locality": "London",
                                "postal_code": "W1A 1AA",
                                "premises": "London Broadcasting House Bbc Club Sports And Leisure Ltd",
                                "address_line_1": "Portland Place",
                                "country": "England"
                            },
                            "officers": [
                                {
                                    "name": "CARTER, Gill",
                                    "officer_role": "director",
                                    "appointed_on": "2017-01-12",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W1A 1AA",
                                        "premises": "London Broadcasting House",
                                        "address_line_1": "Bbc Club Sports And Leisure Ltd",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "COOK, Ross Howard",
                                    "officer_role": "director",
                                    "appointed_on": "2014-11-06",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W1A 1AA",
                                        "premises": "London Broadcasting House",
                                        "address_line_1": "Bbc Club Sports And Leisure Ltd",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "JORDAN, David James",
                                    "officer_role": "director",
                                    "appointed_on": "2010-12-15",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W1A 1AA",
                                        "premises": "London Broadcasting House",
                                        "address_line_1": "Bbc Club Sports And Leisure Ltd",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "KIDD, Andrea Violet",
                                    "officer_role": "director",
                                    "appointed_on": "2015-03-24",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W1A 1AA",
                                        "premises": "Wogan House",
                                        "address_line_1": "Bbc Club Wogan House",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "MOUCHTEREM, Achmet",
                                    "officer_role": "director",
                                    "appointed_on": "2014-03-05",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W1A 1AA",
                                        "premises": "London Broadcasting House",
                                        "address_line_1": "Bbc Club Sports And Leisure Ltd",
                                        "country": "England"
                                    }
                                },
                                {
                                    "name": "WINFIELD, John",
                                    "officer_role": "director",
                                    "appointed_on": "2010-04-01",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W1A 1AA",
                                        "premises": "London Broadcasting House",
                                        "address_line_1": "Bbc Club Sports And Leisure Ltd",
                                        "country": "England"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "13005466",
                            "company_type": "ltd",
                            "title": "BBC COFFEE LIMITED",
                            "company_status": "active",
                            "date_of_creation": "2020-11-09",
                            "address": {
                                "locality": "Southampton",
                                "postal_code": "SO16 0BT",
                                "premises": "Adanac Business Park, Adanac North, Unit E3 & E4,",
                                "address_line_1": "Adanac Drive",
                                "country": "United Kingdom"
                            },
                            "officers": [
                                {
                                    "name": "BODIAM, Kane Ronald Vasudevan",
                                    "officer_role": "director",
                                    "appointed_on": "2022-02-20",
                                    "address": {
                                        "locality": "Southampton",
                                        "postal_code": "SO16 0BT",
                                        "premises": "Adanac Business Park,",
                                        "address_line_1": "Adanac North, Unit E3 & E4,",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "GOLDKORN, Benjamin John, Director",
                                    "officer_role": "director",
                                    "appointed_on": "2021-01-01",
                                    "address": {
                                        "locality": "Southampton",
                                        "postal_code": "SO16 0BT",
                                        "premises": "Adanac Business Park,",
                                        "address_line_1": "Adanac North, Unit E3 & E4,",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "KOCHHAR, Dhruv Neeraj",
                                    "officer_role": "director",
                                    "appointed_on": "2020-11-09",
                                    "address": {
                                        "locality": "Southampton",
                                        "postal_code": "SO16 0BT",
                                        "premises": "Adanac Business Park,",
                                        "address_line_1": "Adanac North, Unit E3 & E4,",
                                        "country": "United Kingdom"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "09158624",
                            "company_type": "ltd",
                            "title": "BBC COMEDY PRODUCTIONS LIMITED",
                            "company_status": "active",
                            "date_of_creation": "2014-08-01",
                            "address": {
                                "locality": "London",
                                "postal_code": "W12 7FA",
                                "premises": "1",
                                "address_line_1": "Television Centre",
                                "country": "United Kingdom"
                            },
                            "officers": [
                                {
                                    "name": "RYLAND, Jackline",
                                    "officer_role": "secretary",
                                    "appointed_on": "2023-08-24",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1 Television Centre",
                                        "address_line_1": "101 Wood Lane",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "ESCOTT, Sarah",
                                    "officer_role": "director",
                                    "appointed_on": "2024-03-28",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1 Television Centre",
                                        "address_line_1": "101 Wood Lane",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "MELLORS, Holly",
                                    "officer_role": "director",
                                    "appointed_on": "2023-02-01",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1 Television Centre",
                                        "address_line_1": "101 Wood Lane",
                                        "country": "United Kingdom"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "04463534",
                            "company_type": "ltd",
                            "title": "BBC COMMERCIAL LIMITED",
                            "company_status": "active",
                            "date_of_creation": "2002-06-18",
                            "address": {
                                "locality": "London",
                                "postal_code": "W12 7FA",
                                "premises": "1",
                                "address_line_1": "Television Centre",
                                "country": "United Kingdom"
                            },
                            "officers": [
                                {
                                    "name": "CORRIETTE, Anthony",
                                    "officer_role": "secretary",
                                    "appointed_on": "2022-04-01",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1 Television Centre",
                                        "address_line_1": "101 Wood Lane",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "BHOW, Gunjan Dhanesh",
                                    "officer_role": "director",
                                    "appointed_on": "2022-04-01",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1",
                                        "address_line_1": "Television Centre",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "BUFFINI, Damon Marcus",
                                    "officer_role": "director",
                                    "appointed_on": "2022-04-01",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1",
                                        "address_line_1": "Television Centre",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "BURGESS, Lorraine Yasmin",
                                    "officer_role": "director",
                                    "appointed_on": "2022-04-01",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1",
                                        "address_line_1": "Television Centre",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "FUSSELL, Thomas Cyrus",
                                    "officer_role": "director",
                                    "appointed_on": "2017-09-18",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1 Television Centre",
                                        "address_line_1": "101 Wood Lane",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "FYFIELD, Rowenna Mai",
                                    "officer_role": "director",
                                    "appointed_on": "2019-07-18",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1",
                                        "address_line_1": "Television Centre",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "GRIFFITHS, Ian Ward",
                                    "officer_role": "director",
                                    "appointed_on": "2023-04-01",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1",
                                        "address_line_1": "Television Centre",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "HUNGATE, Claire",
                                    "officer_role": "director",
                                    "appointed_on": "2023-04-01",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1",
                                        "address_line_1": "Television Centre",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "NEWMAN, Gary Stephen",
                                    "officer_role": "director",
                                    "appointed_on": "2023-04-01",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1",
                                        "address_line_1": "Television Centre",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "SINGH, Bhavneet",
                                    "officer_role": "director",
                                    "appointed_on": "2022-04-01",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1",
                                        "address_line_1": "Television Centre",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "TAVAZIVA, Anne Leigh",
                                    "officer_role": "director",
                                    "appointed_on": "2021-03-25",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W12 7FA",
                                        "premises": "1",
                                        "address_line_1": "Television Centre",
                                        "country": "United Kingdom"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "13637184",
                            "company_type": "ltd",
                            "title": "BBC CONCRETE PRODUCTS LTD",
                            "company_status": "active",
                            "date_of_creation": "2021-09-22",
                            "address": {
                                "locality": "Dewsbury",
                                "postal_code": "WF12 9QB",
                                "premises": "Cartwright Mill",
                                "address_line_1": "Watergate Road",
                                "country": "England"
                            },
                            "officers": [
                                {
                                    "name": "HAFEEZ, Abdul",
                                    "officer_role": "director",
                                    "appointed_on": "2021-09-22",
                                    "address": {
                                        "locality": "Dewsbury",
                                        "postal_code": "WF12 9QB",
                                        "premises": "Cartwright Mill",
                                        "address_line_1": "Watergate Road",
                                        "country": "England"
                                    }
                                }
                            ]
                        },
                        {
                            "company_number": "13903907",
                            "company_type": "ltd",
                            "title": "BBC CONSERVATOIRE OF MUSIC & MATHEMATICS LTD",
                            "company_status": "active",
                            "date_of_creation": "2022-02-09",
                            "address": {
                                "locality": "London",
                                "postal_code": "W11 3JE",
                                "premises": "22",
                                "address_line_1": "Notting Hill Gate",
                                "country": "United Kingdom"
                            },
                            "officers": [
                                {
                                    "name": "DEAL CHAMBERS LTD",
                                    "officer_role": "corporate-secretary",
                                    "appointed_on": "2022-02-09",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W11 3JE",
                                        "premises": "22",
                                        "address_line_1": "Notting Hill Gate",
                                        "country": "United Kingdom"
                                    }
                                },
                                {
                                    "name": "TAYLOR, Beth, Dr",
                                    "officer_role": "director",
                                    "appointed_on": "2022-02-09",
                                    "address": {
                                        "locality": "London",
                                        "postal_code": "W11 3JE",
                                        "premises": "22",
                                        "address_line_1": "Notting Hill Gate",
                                        "country": "United Kingdom"
                                    }
                                }
                            ]
                        }
                    ]
                }""";
    }

}
