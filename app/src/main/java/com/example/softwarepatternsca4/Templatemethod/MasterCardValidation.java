package com.example.softwarepatternsca4.Templatemethod;

public class MasterCardValidation extends CardValidator {
    public MasterCardValidation(String cardName, String cardNumber, String pin) {
        super(cardName, cardNumber, pin);
    }

    protected boolean validateCardNumberFormat() {
        boolean errorInFormat = false;

        if (cardNumber.charAt(0) == '5' && (cardNumber.charAt(1) >= '1' && cardNumber.charAt(1) <= '5')) {
        }
        else {
            errorInFormat = true;
        }
        return !errorInFormat;
    }
}