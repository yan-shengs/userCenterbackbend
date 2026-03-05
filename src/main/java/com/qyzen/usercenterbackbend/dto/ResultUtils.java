package com.qyzen.usercenterbackbend.dto;

public class ResultUtils {

    public static <T> Result<T> success(T data) {
        return new Result<T>(0, data, "success", null);
    };
    
    public static <T> Result<T> success(ResultCode code, T data) {
        return new Result<T>(code.getCode(), data, code.getMessage(), code.getDescription());
    };

    public static <T> Result<T> error(int code, T data, String description) {
        return new Result<T>(code, null, "error", description);
    };

    public static Result<Void> error(int code, String message, String description) {
        return new Result<Void>(code, null, message, description);
    }

    public static <T> Result<T> error(ResultCode code) {
        return new Result<T>(code.getCode(), null, code.getMessage(), code.getDescription());
    };


}
