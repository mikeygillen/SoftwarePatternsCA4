package com.example.softwarepatternsca4.Templatemethod;

public abstract class CardValidator {
    protected String cardName;
    protected String cardNumber;
    protected String pin;

    public CardValidator(String cardName, String cardNumber, String pin) {
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    public boolean validate() {
        boolean cardNameValidated = validateCardName();

        if (cardNameValidated) {
                boolean pinValidated = validatePin();
                if (pinValidated) {
                    boolean cardNumberLengthValidated = validateCardNumberLength();
                    if (cardNumberLengthValidated) {
                        boolean cardNumberFormatValidated = validateCardNumberFormat();
                        if (cardNameValidated && cardNumberLengthValidated && cardNumberFormatValidated)
                            return true;
                        else
                            return false;
                    }
                }
        }
        return false;
    }

    protected boolean validateCardName() {
        boolean errorInName = false;

        if (cardName.length() == 0) {
            errorInName = true;
        }
        return !errorInName;
    }

    protected boolean validatePin() {
        boolean errorInCvv = false;

        if (pin.length() != 3 && pin.length() != 4) {
            errorInCvv = true;
        }
        return !errorInCvv;
    }

    protected boolean validateCardNumberLength() {
        boolean errorInNumber = false;

        if (cardNumber.length() != 16) {
            errorInNumber = true;
        } else {
            for (int i = 0; i < 16; i++) {
                if (cardNumber.charAt(i) > '9' || cardNumber.charAt(i) < '0') {
                    errorInNumber = true;
                }
            }
        }
        return !errorInNumber;
    }

    protected boolean validateCardNumberFormat() {
        return false;
    }
}