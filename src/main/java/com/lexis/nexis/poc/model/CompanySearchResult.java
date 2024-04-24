package com.lexis.nexis.poc.model;

import java.util.List;

public record CompanySearchResult(int total_results, List<Company> items) {
}