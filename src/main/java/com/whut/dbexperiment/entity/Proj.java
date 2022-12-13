package com.whut.dbexperiment.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString
public class Proj implements Serializable {

    //项目id
    private Long projId;

    //项目状态
    private String projStatus;

    //项目包含的人员数
    private Integer userCount;

    //项目开始时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime beginTime;

    //项目结束时间
    private LocalDateTime endTime;

    //项目名
    private String projName;

}
