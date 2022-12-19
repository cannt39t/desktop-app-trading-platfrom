package com.example.models;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class StockGlass {

    private LinkedHashMap<String, String> priceToVolume;

    public StockGlass(LinkedHashMap<String, String> priceToVolume) {
        this.priceToVolume = priceToVolume;
    }

    @Override
    public String toString() {
        return "StockGlass{" +
                "priceToVolume=" + priceToVolume +
                '}';
    }

    public LinkedHashMap<String, String> getPriceToVolume() {
        return priceToVolume;
    }

    public void setPriceToVolume(LinkedHashMap<String, String> priceToVolume) {
        this.priceToVolume = priceToVolume;
    }
}
