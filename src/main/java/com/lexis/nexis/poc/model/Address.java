package com.lexis.nexis.poc.model;

public record Address(String locality,
                      String postal_code,
                      String premises,
                      String address_line_1,
                      String country) {
}