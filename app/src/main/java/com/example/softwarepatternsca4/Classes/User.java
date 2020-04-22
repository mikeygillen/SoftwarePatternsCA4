package com.example.softwarepatternsca4.Classes;

public class User {

    private String email, address, discount;


    public User() {
    }

    public User(String email, String address, String discount) {
        this.email = email;
        this.address = address;
        this.discount = discount;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
