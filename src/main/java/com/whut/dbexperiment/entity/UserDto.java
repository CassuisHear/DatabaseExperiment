package com.whut.dbexperiment.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 此类是User的子类，
 * 但是含有Proj的集合属性，
 * 专门用来展示人员信息和人员所属的多个项目
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDto extends User {

    private List<String> projs;
}
