package com.example.bankaccount.controllers.exceptions;

public class BankAccountNotFoundException extends Exception{
    public BankAccountNotFoundException(String msg){
        super(msg);
    }
}
