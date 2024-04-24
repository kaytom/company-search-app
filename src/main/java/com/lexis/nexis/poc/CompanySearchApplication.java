package com.lexis.nexis.poc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CompanySearchApplication {
    private static final Logger logger = LogManager.getLogger(CompanySearchApplication.class);

    public static void main(String[] args) {
        logger.info("Starting the Company Search Application");
        SpringApplication.run(CompanySearchApplication.class, args);
    }

}
