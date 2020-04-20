package com.example.softwarepatternsca4;

public class User {

    private String email, address, payment;


    public User(){
    }

    public User(String email, String address, String payment) {
        this.email = email;
        this.address = address;
        this.payment = payment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
