package com.example.models;

public class LimitOrder {

    private int id;
    private String side;
    private int volume;
    private int atPrice;
    private int userId;
    private int quotationId;

    @Override
    public String toString() {
        return "LimitOrder{" +
                "id=" + id +
                ", side='" + side + '\'' +
                ", volume=" + volume +
                ", atPrice=" + atPrice +
                ", userId=" + userId +
                ", quotationId=" + quotationId +
                '}';
    }

    public LimitOrder(int id, String side, int volume, int atPrice, int userId, int quotationId) {
        this.id = id;
        this.side = side;
        this.volume = volume;
        this.atPrice = atPrice;
        this.userId = userId;
        this.quotationId = quotationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getAtPrice() {
        return atPrice;
    }

    public void setAtPrice(int atPrice) {
        this.atPrice = atPrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(int quotationId) {
        this.quotationId = quotationId;
    }
}
