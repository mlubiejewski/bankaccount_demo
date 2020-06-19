package com.example.bankaccount.clients;

import com.example.bankaccount.controllers.dtos.CurrencyRatesDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CurrencyRatesClient {

    private final String baseUrl = "https://api.nbp.pl/api/exchangerates/rates/a/usd/";

    private RestTemplate restTemplate;

    public BigDecimal getUsdRate(){
        ResponseEntity<CurrencyRatesDTO> response = restTemplate.getForEntity(baseUrl, CurrencyRatesDTO.class);
        return response.getBody().getRates().get(0).getMid();

    }

}



