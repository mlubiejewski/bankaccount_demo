package com.example.bankaccount.controllers.exceptions;

public class SameCurrenciesException extends Exception{
    public SameCurrenciesException(String msg){
        super(msg);
    }
}
