package com.example.softwarepatternsca4.Classes;

import java.util.ArrayList;

public class Order {
    private String user, shipping, payment, products;
    //private ArrayList<String> products;

    public Order() {
    }

    public Order(String user, String shipping, String payment, String products) {
        this.user = user;
        this.shipping = shipping;
        this.payment = payment;
        this.products = products;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}
