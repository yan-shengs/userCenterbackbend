package com.qyzen.usercenterbackbend.controller;

import com.qyzen.usercenterbackbend.dto.ResultCode;
import com.qyzen.usercenterbackbend.dto.Result;
import com.qyzen.usercenterbackbend.dto.request.RequestLoginDto;
import com.qyzen.usercenterbackbend.dto.request.RequestRegisterDto;
import com.qyzen.usercenterbackbend.dto.response.ResponseUserVo;
import com.qyzen.usercenterbackbend.exception.BussinessException;
import com.qyzen.usercenterbackbend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class UserController {

    private final UserService userservice;

    public UserController(UserService userservice) {
        this.userservice = userservice;
    }

    @PostMapping("/login")
    public Result<ResponseUserVo> login(@RequestBody RequestLoginDto requestLoginDto, HttpServletRequest request) {
        if (requestLoginDto == null) {
            throw new BussinessException(ResultCode.ISParameter);
        }

        String username = requestLoginDto.getUsername();
        String password = requestLoginDto.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BussinessException(ResultCode.ISParameter);
        }

        return userservice.login(username, password, request);
    }

    @PostMapping("/register")
    public Result<ResponseUserVo> register(@RequestBody RequestRegisterDto requestRegisterDto, HttpServletRequest request) {
        if (requestRegisterDto == null) {
            throw new BussinessException(ResultCode.ISParameter);
        }

        String username = requestRegisterDto.getUsername();
        String password = requestRegisterDto.getPassword();
        String passwordDouble = requestRegisterDto.getPasswordDouble();
        if (StringUtils.isAnyBlank(username, password, passwordDouble)) {
            throw new BussinessException(ResultCode.ISParameter);
        }
        return userservice.register(username, password, passwordDouble, request);
    }

    @GetMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        if (request == null) {
            throw new BussinessException(ResultCode.ISParameter);
        }
        return userservice.logout(request);
    }

    @GetMapping("/current")
    public Result<ResponseUserVo> current(HttpServletRequest request) {
        if (request == null) {
            throw new BussinessException(ResultCode.ISParameter);
        }
        return userservice.current(request);
    }

    @GetMapping("/search")
    public Result<List<ResponseUserVo>> search(HttpServletRequest request, String username) {
        if (request == null) {
            throw new BussinessException(ResultCode.ISParameter);
        }
        if (StringUtils.isBlank(username)) {
            throw new BussinessException(ResultCode.ISParameter);
        }
        return userservice.search(request, username);
    }

    @PutMapping("/delete")
    public Result<String> delete(HttpServletRequest request,String username) {
        if (request == null) {
            throw new BussinessException(ResultCode.ISParameter);
        }
        if (StringUtils.isBlank(username)) {
            throw new BussinessException(ResultCode.ISParameter);
        }
        return userservice.delete(request, username);
    }
}
