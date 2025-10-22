package com.infosys.inventory.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPService {
    private static final Map<String, String> otpStorage = new HashMap<>();

    public static String generateOTP(String email){
        String otp = String.format("%06d",new Random().nextInt(999999));
        otpStorage.put(email,otp);
        return otp;
    }

    public static boolean validateOTP(String email, String enteredOTP){
        String storedOTP = otpStorage.get(email);
        if(storedOTP != null && storedOTP.equals(enteredOTP)){
            otpStorage.remove(email);
            return true;
        }
        return false;
    }
}
