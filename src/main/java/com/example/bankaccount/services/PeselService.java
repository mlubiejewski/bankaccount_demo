package com.example.bankaccount.services;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class PeselService {

    public boolean isValid(String pesel) {

        if (pesel == null) {
            return false;
        }

        if (pesel.length() != 11) {
            return false;
        }

        if (!pesel.matches("[0-9]+")){
            return false;
        }

        return true;
    }


    public Date getDate(String pesel) {
        int year = Integer.parseInt(pesel.substring(0, 2), 10);
        int month = Integer.parseInt(pesel.substring(2, 4), 10) - 1;
        int day = Integer.parseInt(pesel.substring(4, 6), 10);

        if (month > 80) {
            year = year + 1800;
            month = month - 80;
        } else if (month > 60) {
            year = year + 2200;
            month = month - 60;
        } else if (month > 40) {
            year = year + 2100;
            month = month - 40;
        } else if (month > 20) {
            year = year + 2000;
            month = month - 20;
        } else {
            year += 1900;
        }

        return new GregorianCalendar(year, month, day).getTime();

    }
}
