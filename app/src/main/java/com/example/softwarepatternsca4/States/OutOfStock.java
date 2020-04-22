package com.example.softwarepatternsca4.States;

public class OutOfStock implements StockState{

    public boolean stateOfStock() {
        return false;
    }

}
