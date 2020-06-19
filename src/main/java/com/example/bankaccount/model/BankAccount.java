package com.example.bankaccount.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Data
public class BankAccount {

    @Id
    @Size(min = 11, max=11, message = "Pesel's length should be 11")
    private String pesel;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastname;
    @DecimalMin(value = "0.00", message = "The amount cannot be negative")
    private BigDecimal plnAmount;
    private BigDecimal usdAmount;

    public boolean userIsTooYoung(Date date){
        return Period.between(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears() < 18;
    }

}
