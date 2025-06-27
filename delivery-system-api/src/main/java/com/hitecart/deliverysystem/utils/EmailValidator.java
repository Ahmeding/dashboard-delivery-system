package com.hitecart.deliverysystem.utils;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+)*@[a-zA-Z0-9.-]+$"
    );
    public static boolean isValidEmail(String email){
        if(email==null || email.isEmpty()){
        return false;
        }
        return EMAIL_REGEX.matcher(email).matches();
    }
}
