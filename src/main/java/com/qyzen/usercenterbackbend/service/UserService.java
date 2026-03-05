package com.qyzen.usercenterbackbend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qyzen.usercenterbackbend.domain.User;
import com.qyzen.usercenterbackbend.dto.Result;
import com.qyzen.usercenterbackbend.dto.response.ResponseUserVo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService extends IService<User> {

    Result<ResponseUserVo> login(String username, String password, HttpServletRequest request);

    Result<ResponseUserVo> register(String username, String userPassword, String userPasswordDouble, HttpServletRequest request);

    Result<String> logout(HttpServletRequest request);

    Result<ResponseUserVo> current(HttpServletRequest request);

    Result<List<ResponseUserVo>> search(HttpServletRequest request, String username);

    Result<String> delete(HttpServletRequest request, String username);
}
