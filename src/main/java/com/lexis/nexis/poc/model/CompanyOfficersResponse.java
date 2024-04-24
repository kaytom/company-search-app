package com.lexis.nexis.poc.model;

import java.util.List;

public record CompanyOfficersResponse(
        String etag,
        int inactive_count,
        Links links,
        String kind,
        int items_per_page,
        List<Officer> items,
        int total_results,
        int resigned_count
) {
}

