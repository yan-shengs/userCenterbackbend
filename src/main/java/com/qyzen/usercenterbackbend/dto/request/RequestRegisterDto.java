package com.qyzen.usercenterbackbend.dto.request;

import lombok.Data;

@Data
public class RequestRegisterDto {

    private String username;

    private String password;

    private String passwordDouble;
}
