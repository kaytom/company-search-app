package com.lexis.nexis.poc.model;

import java.util.List;

public record Company(String company_number,
                      String company_type,
                      String title,
                      String company_status,
                      String date_of_creation,
                      Address address,
                      List<OfficerView> officers) {
}