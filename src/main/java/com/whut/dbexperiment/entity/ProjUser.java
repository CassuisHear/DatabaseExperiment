package com.whut.dbexperiment.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ProjUser implements Serializable {

    //项目id
    private Long projId;

    //用户id
    private Long userId;

    //这个人员负责的部分
    private String resPart;

    //项目进度
    private String projProgress;
}
