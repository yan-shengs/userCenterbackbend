package com.qyzen.usercenterbackbend.dto;

public enum ResultCode {

    SUCCESS(2000, "操作成功", "业务逻辑操作成功"),
    ISParameter(4000, "操作失败", "通用参数错误"),
    ISTOKEN(4001, "参数过期", "用户未登陆/Token失效"),
    ISAUTH(4002, "无权限", "用户无权限"),
    ISRESOURCE(4003, "请求失败", "请求资源不存在"),
    ISCODE(4004, "请求失败", "验证码/密码错误"),
    SYSTEM_ERROE(5000, "系统错误", "系统发生错误 "),
    ISACCOUNT(6001, "业务错误，注册失败", "账号/可能存在"),
    ISACCOUNTLOGIN(60011, "业务错误，登陆失败", "账号/密码错误");

    private int code;

    private String message;

    private String description;

    ResultCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
