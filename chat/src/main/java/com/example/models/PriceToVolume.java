package com.example.models;

import javafx.beans.property.SimpleStringProperty;

public class PriceToVolume {

    private String price;
    private String volume;

    public PriceToVolume(String price, String volume) {
        this.price = price;
        this.volume = volume;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "PriceToVolume{" +
                "price='" + price + '\'' +
                ", volume='" + volume + '\'' +
                '}';
    }
}
