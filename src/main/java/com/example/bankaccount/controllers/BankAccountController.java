package com.example.bankaccount.controllers;

import com.example.bankaccount.controllers.dtos.ExchangeDTO;
import com.example.bankaccount.controllers.exceptions.*;
import com.example.bankaccount.model.BankAccount;
import com.example.bankaccount.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class BankAccountController {

    private BankAccountService bankAccountService;

    @PostMapping
    public BankAccount createBankAccount(@Valid @RequestBody BankAccount bankAccount) throws BankAccountForPeselAlreadyExistsException, UserAgeNotSatisfiedException, InvalidPeselException {
        return bankAccountService.createBankAccount(bankAccount);
    }

    @GetMapping(value = "/{pesel}")
    public BankAccount findBankAccount(@PathVariable String pesel) throws BankAccountNotFoundException {
        return bankAccountService.findBankAccount(pesel);
    }

    @PutMapping(value = "exchange/{pesel}")
    public BankAccount exchange(@PathVariable String pesel, @RequestBody @Valid ExchangeDTO exchangeDTO) throws BankAccountNotFoundException, InsufficentSourceAmountException {
        return bankAccountService.exchange(pesel, exchangeDTO);
    }

    @ExceptionHandler(BankAccountForPeselAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBankAccountForPeselExistsException(BankAccountForPeselAlreadyExistsException e){
        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());
        return error;
    }

    @ExceptionHandler(UserAgeNotSatisfiedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUserAgeNotSatisfiedException(UserAgeNotSatisfiedException e){
        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());
        return error;
    }

    @ExceptionHandler(BankAccountNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBankAccountNotFoundException(BankAccountNotFoundException e){
        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());
        return error;
    }

    @ExceptionHandler(InsufficentSourceAmountException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInsufficentSourceAmountException(InsufficentSourceAmountException e){
        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());
        return error;
    }

    @ExceptionHandler(InvalidPeselException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidPeselException(InvalidPeselException e){
        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());
        return error;
    }


}
