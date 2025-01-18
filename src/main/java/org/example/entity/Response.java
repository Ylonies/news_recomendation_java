package org.example.entity;

public class Response<T> {
    private int statusCode;
    private T data;
    public Response(int errorCode, T data) {
        this.statusCode = errorCode;
        this.data = data;
    }

    public Response(int errorCode){
        this.statusCode = errorCode;
        this.data = null;
    }
    public int getStatusCode() {
        return statusCode;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return statusCode == 200;
    }
}