package org.example.dto;

public class Response<T> {
    private int errorCode;
    private T data;
    public Response(int errorCode, T data) {
        this.errorCode = errorCode;
        this.data = data;
    }

    public Response(int errorCode){
        this.errorCode = errorCode;
        this.data = null;
    }
    public int getErrorCode() {
        return errorCode;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return errorCode == 200;
    }
}