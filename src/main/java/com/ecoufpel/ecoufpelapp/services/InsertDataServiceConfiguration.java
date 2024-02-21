package com.ecoufpel.ecoufpelapp.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InsertDataServiceConfiguration {
    @Bean
    public InsertDataService insertDataService() {
        return new InsertDataService();
    }
}
