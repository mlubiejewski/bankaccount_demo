package com.example.bankaccount.controllers.exceptions;

public class UserAgeNotSatisfiedException extends Exception{
    public UserAgeNotSatisfiedException(String msg){
        super(msg);
    }
}
