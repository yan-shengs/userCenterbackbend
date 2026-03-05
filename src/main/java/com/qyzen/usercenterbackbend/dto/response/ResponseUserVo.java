package com.qyzen.usercenterbackbend.dto.response;

import lombok.Data;

@Data
public class ResponseUserVo {

    private int id;

    private String username;

    private int isAdmin;
}
