package com.derysuwandi.restapisimplebanking.exception;

public class InfoLevelException extends RuntimeException {
    private String status = "4000";
    private Object data;

    public InfoLevelException() {
        super("invalid request");
    }

    public InfoLevelException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public InfoLevelException(String message) {
        super(message);
    }

    public InfoLevelException (String message, String status) {
        super(message);
        this.status = status;
    }

    public InfoLevelException (String message, String status, Object data) {
        super(message);
        this.status = status;
        this.data = data;
    }

    public static InfoLevelException create(String message) {
        return new InfoLevelException(message);
    }

    public static InfoLevelException create (String message, String status) {
        return new InfoLevelException(message, status);
    }

    public static InfoLevelException create (String message, String status, Object data) {
        return new InfoLevelException(message, status, data);
    }

    public JsonResponse<?> generateJsonResponse() {
        return JsonResponse.get(status, this.getMessage(), this.data);
    }

}
