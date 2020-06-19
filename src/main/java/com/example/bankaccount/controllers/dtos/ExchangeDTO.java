package com.example.bankaccount.controllers.dtos;

import lombok.Value;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Value
public class ExchangeDTO {
    private CurrencyDTO fromCurrency;
    @DecimalMin(value = "0.00", inclusive = false, message = "The amount must be positive")
    private BigDecimal amount;
    private CurrencyDTO toCurrency;
}
