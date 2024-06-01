package com.derysuwandi.restapisimplebanking.utils;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Random;

public class GeneralUtils {

    public static Integer randomAccountNumber(){
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    public static boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

}
