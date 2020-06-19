package com.example.bankaccount.controllers.exceptions;

public class BankAccountForPeselAlreadyExistsException extends Exception{
    public BankAccountForPeselAlreadyExistsException(String msg){
        super(msg);
    }
}
