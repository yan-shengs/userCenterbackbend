package com.qyzen.usercenterbackbend.exception;

import com.qyzen.usercenterbackbend.dto.ResultCode;
import lombok.Data;

@Data
public class BussinessException extends RuntimeException {

    private final int code;

    private final String description;

    public BussinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BussinessException(ResultCode result) {
        super(result.getMessage());
        this.code = result.getCode();
        this.description = result.getDescription();
    }

    public BussinessException(ResultCode result, String description) {
        super(result.getMessage());
        this.code = result.getCode();
        this.description = description;
    }

}
