package com.lexis.nexis.poc.model;

import java.util.List;

public record CompanyResponseItem(
        String company_status,
        String address_snippet,
        String date_of_creation,
        Matches matches,
        String description,
        Links links,
        String company_number,
        String title,
        String company_type,
        Address address,
        String kind,
        List<String> description_identifier,
        String date_of_cessation
) {
}