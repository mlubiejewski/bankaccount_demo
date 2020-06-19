package com.example.bankaccount.services;

import com.example.bankaccount.clients.CurrencyRatesClient;
import com.example.bankaccount.controllers.dtos.CurrencyDTO;
import com.example.bankaccount.controllers.dtos.ExchangeDTO;
import com.example.bankaccount.controllers.exceptions.*;
import com.example.bankaccount.model.BankAccount;
import com.example.bankaccount.repositories.BankAccountRepository;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class BankAccountServiceTest {

    private CurrencyRatesClient currencyRatesClient = mock(CurrencyRatesClient.class);
    private PeselService peselService = mock(PeselService.class);
    private BankAccountRepository bankAccountRepository = mock(BankAccountRepository.class);

    private BankAccountService bankAccountService =
            new BankAccountService(bankAccountRepository, currencyRatesClient, peselService);

    @Test
    public void shouldCreateNewValidBankAccount() throws BankAccountForPeselAlreadyExistsException, UserAgeNotSatisfiedException, InvalidPeselException, ParseException {
        BankAccount bankAccount = getBankAccount();

        DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
        Date date = format.parse("2000-01-01");

        given(peselService.isValid(anyString())).willReturn(true);
        given(peselService.getDate(anyString())).willReturn(date);

        ArgumentCaptor<BankAccount> bankAccountArgumentCaptor = ArgumentCaptor.forClass(BankAccount.class);

        bankAccountService.createBankAccount(bankAccount);

        then(bankAccountRepository).should().save(bankAccountArgumentCaptor.capture());
        BDDAssertions.then(bankAccountArgumentCaptor.getValue().getUsdAmount()).isEqualTo(BigDecimal.ZERO);

    }

    @Test
    public void shouldExchangeCurrencyIfAvailable() throws BankAccountNotFoundException, InsufficentSourceAmountException {
        BankAccount bankAccount = getBankAccount();
        bankAccount.setUsdAmount(BigDecimal.ZERO);

        given(bankAccountRepository.findById("1")).willReturn(Optional.of(bankAccount));
        given(currencyRatesClient.getUsdRate()).willReturn(BigDecimal.valueOf(4));

        ArgumentCaptor<BankAccount> bankAccountArgumentCaptor = ArgumentCaptor.forClass(BankAccount.class);

        bankAccountService.exchange("1", getExchangeDTO());

        then(bankAccountRepository).should().save(bankAccountArgumentCaptor.capture());
        BDDAssertions.then(bankAccountArgumentCaptor.getValue().getPlnAmount()).isEqualTo(BigDecimal.valueOf(900));
        BDDAssertions.then(bankAccountArgumentCaptor.getValue().getUsdAmount()).isEqualTo(BigDecimal.valueOf(25.00).setScale(2));

    }

    private BankAccount getBankAccount(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setPlnAmount(BigDecimal.valueOf(1000));
        bankAccount.setPesel("1");
        bankAccount.setLastname("Kowalski");
        bankAccount.setName("Jan");
        return bankAccount;
    }

    private ExchangeDTO getExchangeDTO(){
        return new ExchangeDTO(CurrencyDTO.PLN, BigDecimal.valueOf(100), CurrencyDTO.USD);
    }


}
