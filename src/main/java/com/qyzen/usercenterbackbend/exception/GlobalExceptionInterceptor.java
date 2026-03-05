package com.qyzen.usercenterbackbend.exception;

import com.qyzen.usercenterbackbend.dto.ResultCode;
import com.qyzen.usercenterbackbend.dto.Result;
import com.qyzen.usercenterbackbend.dto.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionInterceptor {

    @ExceptionHandler(BussinessException.class)
    public Result<?> BussinessExceptionHandler(BussinessException e) {
        log.info("BussinessException is "+ e.getMessage());
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<?> RuntimeExceptionHandler(RuntimeException e) {
        log.info("RuntimeException is "+ e.getMessage());
        return ResultUtils.error(ResultCode.SYSTEM_ERROE);
    }
}
