package com.qyzen.usercenterbackbend.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private int code;

    private T data;

    private String message;

    private String description;

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public Result(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Result(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public Result(ResultCode result, T data) {
        this.code = result.getCode();
        this.data = data;
        this.message = result.getMessage();
        this.description = result.getDescription();
    }

    public Result(ResultCode result, T data, String description) {
        this.code = result.getCode();
        this.data = data;
        this.message = result.getMessage();
        this.description = description;
    }
}
