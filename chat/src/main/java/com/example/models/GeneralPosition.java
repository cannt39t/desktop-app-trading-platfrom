package com.example.models;

public class GeneralPosition {

    private int userId;
    private String side;
    private int avgVolume;
    private int avgPrice;
    private int pnl;

    public GeneralPosition(int userId, String side, int avgVolume, int avgPrice, int pnl) {
        this.userId = userId;
        this.side = side;
        this.avgVolume = avgVolume;
        this.avgPrice = avgPrice;
        this.pnl = pnl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getAvgVolume() {
        return avgVolume;
    }

    public void setAvgVolume(int avgVolume) {
        this.avgVolume = avgVolume;
    }

    public int getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(int avgPrice) {
        this.avgPrice = avgPrice;
    }

    public int getPnl() {
        return pnl;
    }

    public void setPnl(int pnl) {
        this.pnl = pnl;
    }

    @Override
    public String toString() {
        return "GeneralPosition{" +
                "userId=" + userId +
                ", side='" + side + '\'' +
                ", avgVolume=" + avgVolume +
                ", avgPrice=" + avgPrice +
                ", pnl=" + pnl +
                '}';
    }
}
