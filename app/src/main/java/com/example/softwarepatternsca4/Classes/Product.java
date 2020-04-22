package com.example.softwarepatternsca4.Classes;
import com.google.firebase.database.Exclude;

public class Product {

    private String name, manufacturer, category, image, key;
    private double price;
    private int quantity;
    private boolean state;

    public Product() {
    }

    public Product(String name, String manufacturer, String category, String image, String key, double price, int quantity, boolean state) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.category = category;
        this.image = image;
        this.key = key;
        this.price = price;
        this.quantity = quantity;
        this.state = state;
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

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Exclude
    public String getKey(){ return key; }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

}
