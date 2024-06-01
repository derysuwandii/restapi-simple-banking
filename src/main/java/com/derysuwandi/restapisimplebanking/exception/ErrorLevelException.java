package com.derysuwandi.restapisimplebanking.exception;

public class ErrorLevelException extends RuntimeException {
    private String status = "5000";
    private Object data;

    public ErrorLevelException() {
        super("internal system error");
    }

    public ErrorLevelException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public ErrorLevelException(String message) {
        super(message);
    }

    public ErrorLevelException(String message, String status) {
        super(message);
        this.status = status;
    }

    public ErrorLevelException(String message, String status, Object data) {
        super(message);
        this.status = status;
        this.data = data;
    }

    public static ErrorLevelException create(String message) {
        return new ErrorLevelException(message);
    }

    public static ErrorLevelException create (String message, String status) {
        return new ErrorLevelException(message, status);
    }

    public static ErrorLevelException create (String message, String status, Object data) {
        return new ErrorLevelException(message, status, data);
    }

    public JsonResponse<?> generateJsonResponse() {
        return JsonResponse.get(status, this.getMessage(), this.data);
    }
}
