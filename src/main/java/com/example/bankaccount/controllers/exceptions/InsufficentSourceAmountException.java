package com.example.bankaccount.controllers.exceptions;

public class InsufficentSourceAmountException extends Exception {
    public InsufficentSourceAmountException(String msg){
        super(msg);
    }
}
