package com.example.models;

public class Position {

    private int id;
    private String side;
    private int volume;
    private int atPrice;
    private int userId;
    private int quotationId;
    private boolean closeOrNot;


    public Position(int id, String side, int volume, int atPrice, int userId, int quotationId, boolean closeOrNot) {
        this.id = id;
        this.side = side;
        this.volume = volume;
        this.atPrice = atPrice;
        this.userId = userId;
        this.quotationId = quotationId;
        this.closeOrNot = closeOrNot;
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", side='" + side + '\'' +
                ", volume=" + volume +
                ", atPrice=" + atPrice +
                ", userId=" + userId +
                ", quotationId=" + quotationId +
                ", closeOrNot=" + closeOrNot +
                '}';
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

    public boolean isCloseOrNot() {
        return closeOrNot;
    }

    public void setCloseOrNot(boolean closeOrNot) {
        this.closeOrNot = closeOrNot;
    }

}
