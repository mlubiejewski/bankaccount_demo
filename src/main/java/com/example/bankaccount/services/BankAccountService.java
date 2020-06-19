package com.example.bankaccount.services;

import com.example.bankaccount.clients.CurrencyRatesClient;
import com.example.bankaccount.controllers.dtos.ExchangeDTO;
import com.example.bankaccount.controllers.exceptions.*;
import com.example.bankaccount.model.BankAccount;
import com.example.bankaccount.controllers.dtos.CurrencyDTO;
import com.example.bankaccount.repositories.BankAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class BankAccountService {

    private BankAccountRepository bankAccountRepository;
    private CurrencyRatesClient currencyConverterClient;
    private PeselService peselService;

    public BankAccount createBankAccount(BankAccount bankAccount) throws BankAccountForPeselAlreadyExistsException,
            UserAgeNotSatisfiedException, InvalidPeselException {

        validateBankAccount(bankAccount);

        bankAccount.setUsdAmount(BigDecimal.ZERO);

        return bankAccountRepository.save(bankAccount);
    }

    private void validateBankAccount(BankAccount bankAccount) throws BankAccountForPeselAlreadyExistsException, InvalidPeselException, UserAgeNotSatisfiedException {

        if(!peselService.isValid(bankAccount.getPesel())){
            throw new InvalidPeselException("The pesel " + bankAccount.getPesel() + " is invalid");
        }

        if(!bankAccountRepository.findById(bankAccount.getPesel()).isEmpty()){
            throw new BankAccountForPeselAlreadyExistsException("Bank account for pesel " +
                    bankAccount.getPesel() + " already exists");
        }

        if(bankAccount.userIsTooYoung(peselService.getDate(bankAccount.getPesel()))){
            throw new UserAgeNotSatisfiedException("The user has to be at least 18 years old");
        }

    }

    public BankAccount findBankAccount(String pesel) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(pesel)
                .orElseThrow(() -> new BankAccountNotFoundException("The bank account fot pesel " + pesel + " not found"));
    }

    public BankAccount exchange(String pesel, ExchangeDTO exchangeDTO) throws BankAccountNotFoundException, InsufficentSourceAmountException {

        BankAccount bankAccount = bankAccountRepository.findById(pesel)
                .orElseThrow(() -> new BankAccountNotFoundException("The bank account for pesel " + pesel + " not found"));

        validateExchange(exchangeDTO, bankAccount);

        BigDecimal rate = currencyConverterClient.getUsdRate();

        if (exchangeDTO.getFromCurrency().equals(CurrencyDTO.PLN)){
            bankAccount.setPlnAmount(bankAccount.getPlnAmount().subtract(exchangeDTO.getAmount()));
            bankAccount.setUsdAmount(bankAccount.getUsdAmount().add(exchangeDTO.getAmount().divide(rate, 2, RoundingMode.HALF_DOWN)));
        }
        else {
            bankAccount.setUsdAmount(bankAccount.getUsdAmount().subtract(exchangeDTO.getAmount()));
            bankAccount.setPlnAmount(bankAccount.getPlnAmount().add(exchangeDTO.getAmount().multiply(rate).setScale(2, RoundingMode.HALF_DOWN)));
        }

        return bankAccountRepository.save(bankAccount);
    }

    private void validateExchange(ExchangeDTO exchangeDTO, BankAccount bankAccount) throws InsufficentSourceAmountException {
        if(exchangeDTO.getFromCurrency().equals(exchangeDTO.getToCurrency())){
            throw new RuntimeException("Same currencies!");
        }
        switch (exchangeDTO.getFromCurrency()){
            case PLN:
                if(bankAccount.getPlnAmount().compareTo(exchangeDTO.getAmount())<0){
                    throw new InsufficentSourceAmountException("You don't have enough PLN on your account");
                }
                break;
            case USD:
                if(bankAccount.getUsdAmount().compareTo(exchangeDTO.getAmount())<0){
                    throw new InsufficentSourceAmountException("You don't have enough USD on your account");
                }
        }
    }

}
