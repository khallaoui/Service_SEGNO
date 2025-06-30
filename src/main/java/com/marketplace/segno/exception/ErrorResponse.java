package com.marketplace.segno.exception;


import java.time.LocalDateTime;

public class ErrorResponse {
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    
    public ErrorResponse() {}
    
    public ErrorResponse(String error, String message, LocalDateTime timestamp, String path) {
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
    }
    
    // Getters and Setters
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}