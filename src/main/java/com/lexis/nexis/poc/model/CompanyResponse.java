package com.lexis.nexis.poc.model;

import java.util.List;

public record CompanyResponse(
        int page_number,
        String kind,
        int total_results,
        List<CompanyResponseItem> items
) {
}
