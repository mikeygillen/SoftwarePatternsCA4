package com.example.softwarepatternsca4.Classes;

import java.util.ArrayList;

public class Order {
    private String user, shipping, payment, products;
    private double cost;

    public Order() {
    }

    public Order(String user, String shipping, String payment, String products, double cost) {
        this.user = user;
        this.shipping = shipping;
        this.payment = payment;
        this.products = products;
        this.cost = cost;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
