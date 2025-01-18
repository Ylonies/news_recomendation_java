package org.example.entity;

public class Response<T> {
    private int statusCode;
    private T data;
    private String message = "";

    public Response(int errorCode, T data) {
        this.statusCode = errorCode;
        this.data = data;
    }

    public Response(int errorCode){
        this.statusCode = errorCode;
        this.data = null;
    }

    public Response(int errorCode, String message){
        this.statusCode = errorCode;
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