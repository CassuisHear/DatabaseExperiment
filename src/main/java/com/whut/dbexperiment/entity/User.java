package com.whut.dbexperiment.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Primary;

import java.io.Serializable;

@Data
@ToString
public class User  implements Serializable {

    //用户id
    private Long userId;

    //用户名
    private String username;

    //性别
    private String usersex;

    //密码
    private String password;

}
