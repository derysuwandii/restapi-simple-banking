package com.derysuwandi.restapisimplebanking.exception;

import lombok.ToString;

import java.util.Date;

@ToString
public class JsonResponse<T> {
    public static final String OK_CODE = "0000";
    public static final String ERROR_CODE_REQUEST = "4000";
    public static final String ERROR_CODE_SERVER = "5000";
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    private String code;
    private T data;
    private String message;
    private final Date time = new Date();

    public JsonResponse() {
    }

    public JsonResponse(String status) {
        this.code = status;
    }

    public JsonResponse(String status, T data, String message) {
        this.code = status;
        this.data = data;
        this.message = message;
    }

    public static <T> JsonResponse<T> ok() {
        return new JsonResponse<>(OK_CODE, null, SUCCESS);
    }

    public static <T> JsonResponse<T> ok(T data) {
        return new JsonResponse<>(OK_CODE, data, SUCCESS);
    }

    public static <T> JsonResponse<T> ok(String message) {
        return new JsonResponse<>(OK_CODE, null, message);
    }

    public static <T> JsonResponse<String> okDt(String message) {
        return new JsonResponse<>(OK_CODE, message, SUCCESS);
    }

    public static <T> JsonResponse<T> ok(String message, T data) {
        return new JsonResponse<>(OK_CODE, data, message);
    }

    public static <T> JsonResponse<T> error() {
        return new JsonResponse<>(ERROR_CODE_SERVER, null, ERROR);
    }

    public static <T> JsonResponse<T> error(String message) {
        return new JsonResponse<>(ERROR_CODE_SERVER, null, message);
    }

    public static <T> JsonResponse<T> error(String message, String status) {
        return new JsonResponse<>(status, null, message);
    }

    public static <T> JsonResponse<T> error(T data) {
        return new JsonResponse<>(ERROR_CODE_SERVER, data, ERROR);
    }

    public static <T> JsonResponse<T> error(String message, T data) {
        return new JsonResponse<>(ERROR_CODE_SERVER, data, message);
    }

    public static <T> JsonResponse<T> get(String status, String message, T data) {
        return new JsonResponse<>(status, data, message);
    }

    public boolean isSuccess() {
        return OK_CODE.equals(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }
}
