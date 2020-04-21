package com.example.softwarepatternsca4.Classes;

public class Review {
    private String productName, comment;
    private double rating;

    public Review() {
    }

    public Review(String productName, String comment, double rating) {
        this.productName = productName;
        this.comment = comment;
        this.rating = rating;
    }

    public Review(String comment, double rating) {
        this.comment = comment;
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
