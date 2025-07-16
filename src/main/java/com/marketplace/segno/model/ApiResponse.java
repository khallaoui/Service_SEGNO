// File: src/main/java/com/marketplace/segno/model/ApiResponse.java
package com.marketplace.segno.model;

public class ApiResponse<T> {
    private boolean success;
    private int status;
    private String message;
    private T data;

    public ApiResponse() {}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
