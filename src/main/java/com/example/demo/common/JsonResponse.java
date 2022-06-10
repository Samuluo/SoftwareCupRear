package com.example.demo.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JsonResponse<R> implements Serializable {
    private static final long serialVersionUID = 7574078101944305355L;
    private boolean status;
    private int code;
    private String message;
    private R data;
    private Map<String, Object> otherData = new HashMap();

    public JsonResponse() {
    }

    public static <R> JsonResponse<R> success(R data) {
        JsonResponse<R> response = new JsonResponse();
        response.status = true;
        response.data = data;
        return response;
    }

    public static <R> JsonResponse<R> success(R data, String message) {
        JsonResponse<R> response = new JsonResponse();
        response.status = true;
        response.data = data;
        response.message = message;
        return response;
    }

    public static <R> JsonResponse<R> successMessage(String message) {
        return message(true, message);
    }

    public static <R> JsonResponse<R> message(boolean status, String message) {
        JsonResponse<R> response = new JsonResponse();
        response.status = status;
        response.message = message;
        return response;
    }

    public static <R> JsonResponse<R> failure(String message) {
        return message(false, message);
    }

    public JsonResponse<R> setOtherData(Map<String, Object> otherData) {
        this.otherData = otherData;
        return this;
    }

    public JsonResponse<R> addOtherData(String key, Object value) {
        this.otherData.put(key, value);
        return this;
    }

    public JsonResponse<R> removeOtherData(String key) {
        this.otherData.remove(key);
        return this;
    }

    public JsonResponse<R> setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public JsonResponse<R> setCode(int code) {
        this.code = code;
        return this;
    }

    public JsonResponse<R> setMessage(String message) {
        this.message = message;
        return this;
    }

    public JsonResponse<R> setData(R data) {
        this.data = data;
        return this;
    }

    public JsonResponse<R> setException(Exception e) {
        this.addOtherData("exception", e.getClass().getName());
        return this;
    }

    public JsonResponse<R> removeException() {
        this.removeOtherData("exception");
        return this;
    }

    public boolean isStatus() {
        return this.status;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public R getData() {
        return this.data;
    }

    public Map<String, Object> getOtherData() {
        return this.otherData;
    }
}

