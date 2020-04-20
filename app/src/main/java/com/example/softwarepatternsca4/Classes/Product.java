package com.example.softwarepatternsca4.Classes;

import com.google.firebase.database.Exclude;

import java.util.Comparator;
import java.util.List;

public class Product {

    private String name, manufacturer, category, image, key;
    private double price;
    private int quantity;

    public Product() {
    }

    public Product(String name, String manufacturer, String category, double price, int quantity, String image) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Exclude
    public String getKey(){ return key; }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
