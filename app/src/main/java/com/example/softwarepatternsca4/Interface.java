package com.example.softwarepatternsca4;

import com.example.softwarepatternsca4.Classes.Product;
import com.example.softwarepatternsca4.Classes.Review;
import com.example.softwarepatternsca4.Classes.User;

import java.util.ArrayList;

public interface Interface {

    ArrayList<User> userList = new ArrayList<>();
    ArrayList<Product> prodList = new ArrayList<>();
    ArrayList<Product> filteredList = new ArrayList<>();
    ArrayList<Product> shoppingList = new ArrayList<>();
    ArrayList<Review> reviews = new ArrayList<>();

}
