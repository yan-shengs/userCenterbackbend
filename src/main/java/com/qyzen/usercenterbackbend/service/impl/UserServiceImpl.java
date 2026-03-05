package com.qyzen.usercenterbackbend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyzen.usercenterbackbend.domain.User;
import com.qyzen.usercenterbackbend.dto.Result;
import com.qyzen.usercenterbackbend.dto.ResultCode;
import com.qyzen.usercenterbackbend.dto.ResultUtils;
import com.qyzen.usercenterbackbend.dto.response.ResponseUserVo;
import com.qyzen.usercenterbackbend.exception.BussinessException;
import com.qyzen.usercenterbackbend.mapper.UserMapper;
import com.qyzen.usercenterbackbend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import java.util.List;
import java.util.ListResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.qyzen.usercenterbackbend.constant.UserConstant.UserLoginStatus;


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;

    private static final String SALT = "qyzen";

    @Override
    public Result<ResponseUserVo> login(String username, String password, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(username, password) ) {
            throw new BussinessException(ResultCode.ISParameter);
        }

        if (username.length() < 5) {
            throw new BussinessException(ResultCode.ISParameter);
        }

        if (password.length() < 8) {
            throw new BussinessException(ResultCode.ISParameter);
        }
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()) {
            throw new BussinessException(ResultCode.ISParameter);
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        QueryWrapper<User> query = new QueryWrapper<>();

        query.eq("username", username).eq("password", encryptPassword);

        User result = userMapper.selectOne(query);

        if (result == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BussinessException(ResultCode.ISACCOUNTLOGIN);
        }

        ResponseUserVo user = userSafety(result);
        request.getSession().setAttribute(UserLoginStatus, user);

        return ResultUtils.success(user);
    }


    @Override
    public Result<ResponseUserVo> register(String username, String userPassword, String userPasswordDouble, HttpServletRequest request) {

        if (StringUtils.isAnyBlank(username, userPassword, userPasswordDouble)) {
            throw new BussinessException(ResultCode.ISACCOUNT);
        }

        if (username.length() < 5) {
            throw new BussinessException(ResultCode.ISACCOUNT);
        }

        if (userPasswordDouble.length() < 8) {
            throw new BussinessException(ResultCode.ISACCOUNT);
        }

        if (!userPassword.equals(userPasswordDouble)) {
            throw new BussinessException(ResultCode.ISACCOUNT);
        }

        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()) {
            throw new BussinessException(ResultCode.ISParameter);
        }

        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", username);
        User result = userMapper.selectOne(query);
        if (result != null) {
            throw new BussinessException(ResultCode.ISACCOUNT);
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        User data = new User();

        data.setUsername(username);
        data.setPassword(encryptPassword);
        data.setNikename(username);
        data.setIsAdmin(0);

        userMapper.insert(data);

        ResponseUserVo user = userSafety(data);
        request.getSession().setAttribute(UserLoginStatus, user);

        return ResultUtils.success(user);
    }

    @Override
    public Result<String> logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new BussinessException(ResultCode.ISTOKEN);
        }
        request.getSession().removeAttribute(UserLoginStatus);
        return ResultUtils.success("注销成功");
    }

    public Result<ResponseUserVo> current(HttpServletRequest request) {
        ResponseUserVo user = (ResponseUserVo) request.getSession().getAttribute(UserLoginStatus);
        if (user == null) {
            throw new BussinessException(ResultCode.ISTOKEN);
        }
        int id = user.getId();
        User result = userMapper.selectById(id);
        ResponseUserVo currentUser = userSafety(result);
        return ResultUtils.success(currentUser);
    }

    public Result<List<ResponseUserVo>> search(HttpServletRequest request, String username) {
        ResponseUserVo user = (ResponseUserVo) request.getSession().getAttribute(UserLoginStatus);
        if (user == null) {
            throw new BussinessException(ResultCode.ISTOKEN);
        }

        int auth = user.getIsAdmin();
        if (!isAdmin(auth)) {
            throw new BussinessException(ResultCode.ISAUTH);
        }
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username" , username);
        List<User> result = userMapper.selectList(query);
        List<ResponseUserVo> list = result.stream().map(r -> userSafety(r)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    public Result<String> delete(HttpServletRequest request, String username) {
        ResponseUserVo user = (ResponseUserVo) request.getSession().getAttribute(UserLoginStatus);
        if (user == null) {
            throw new BussinessException(ResultCode.ISTOKEN);
        }

        int auth = user.getIsAdmin();
        if (!isAdmin(auth)) {
            throw new BussinessException(ResultCode.ISAUTH);
        }
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", username);
        userMapper.delete(query);
        return ResultUtils.success("删除成功");
    }

    public ResponseUserVo userSafety(User originUser) {
        if (originUser == null) {
            throw new BussinessException(ResultCode.ISParameter);
        }
        ResponseUserVo user = new ResponseUserVo();
        user.setId(originUser.getId());
        user.setUsername(originUser.getUsername());
        user.setIsAdmin(originUser.getIsAdmin());
        return user;
    }

    public boolean isAdmin(int auth) {
        if (auth == 1) {
            return true;
        }
        return false;
    }
}




