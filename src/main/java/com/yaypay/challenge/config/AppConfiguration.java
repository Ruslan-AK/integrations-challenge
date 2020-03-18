package com.yaypay.challenge.config;

import com.yaypay.challenge.dtos.InvoiceDTO;
import com.yaypay.challenge.services.DataService;
import com.yaypay.challenge.services.InvoiceDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class AppConfiguration {

    @Bean
    public DataService<InvoiceDTO> invoiceDTODataService(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                                         @Value("${yaypay.challenge.batch.size:1000}") int batchSize) {
        return new InvoiceDataService(namedParameterJdbcTemplate, batchSize);
    }

}
