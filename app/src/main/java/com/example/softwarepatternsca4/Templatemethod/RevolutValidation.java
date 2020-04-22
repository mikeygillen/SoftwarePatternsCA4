package com.example.softwarepatternsca4.Templatemethod;

public class RevolutValidation extends CardValidator {
    public RevolutValidation(String cardName, String cardNumber, String pin) {
        super(cardName, cardNumber, pin);
    }

    protected boolean validateCardNumberFormat() {
        boolean errorInFormat = false;
        if (cardNumber.charAt(0) == '3' && (cardNumber.charAt(1) == '4' || cardNumber.charAt(1) == '7')) {
        } else {
            errorInFormat = true;
        }
        return !errorInFormat;
    }
}