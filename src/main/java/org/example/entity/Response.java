package org.example.entity;

public class Response<T> {
    private final int statusCode;
    private final T data;
    private String message = "";

    public Response(T data) {
        this.statusCode = 200;
        this.data = data;
    }

    public Response(int statusCode) {
        this.statusCode = statusCode;
        this.data = null;
    }
    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = null;
    }


    public int getStatusCode() {
        return statusCode;
    }
    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return statusCode == 200;
    }
}