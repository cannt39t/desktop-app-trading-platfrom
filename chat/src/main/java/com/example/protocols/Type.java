package com.example.protocols;

public enum Type {

    UPDATE("");



    private final String title;

    Type(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
