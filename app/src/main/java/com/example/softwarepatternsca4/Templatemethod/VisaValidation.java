package com.example.softwarepatternsca4.Templatemethod;

public class VisaValidation extends CardValidator {
    public VisaValidation(String cardName, String cardNumber, String pin) {
        super(cardName, cardNumber, pin);
    }

    protected boolean validateCardNumberFormat() {
        boolean errorInFormat = false;
        if (cardNumber.charAt(0) != '4') {
            errorInFormat = true;
        }
        else {
        }
        return !errorInFormat;
    }
}