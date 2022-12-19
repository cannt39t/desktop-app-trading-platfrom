package com.example.protocols;

import java.util.HashMap;
import java.util.Map;

public class Message {

    Map<String, String> headers;
    Type type;
    String body;

    public Message() {
        headers = new HashMap<>();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Type getType() {
        return type;
    }

    public String getBody() {
        return body;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addHeader (String key, String value) {
        headers.put(key, value);
    }
}
