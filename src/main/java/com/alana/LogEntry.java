package com.alana;

public class LogEntry {
    private String ip;
    private String timestamp;
    private String method;
    private String endpoint;
    private int statusCode;

    public LogEntry(String ip, String timestamp, String method, String endpoint, int statusCode) {
        this.ip = ip;
        this.timestamp = timestamp;
        this.method = method;
        this.endpoint = endpoint;
        this.statusCode = statusCode;
    }

    public String getIp() {
        return ip;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMethod() {
        return method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
