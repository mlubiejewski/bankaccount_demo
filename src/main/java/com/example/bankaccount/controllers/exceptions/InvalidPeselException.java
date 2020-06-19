package com.example.bankaccount.controllers.exceptions;

public class InvalidPeselException extends Exception{
    public InvalidPeselException(String msg){
        super(msg);
    }
}
