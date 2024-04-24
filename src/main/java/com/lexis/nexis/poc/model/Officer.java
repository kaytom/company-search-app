package com.lexis.nexis.poc.model;

public record Officer(String name,
                      String officer_role,
                      String appointed_on,
                      Address address,
                      String resigned_on
) {
}

