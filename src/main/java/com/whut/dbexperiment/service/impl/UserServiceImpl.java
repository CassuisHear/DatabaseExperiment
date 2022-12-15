package com.whut.dbexperiment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.dbexperiment.entity.Proj;
import com.whut.dbexperiment.entity.ProjUser;
import com.whut.dbexperiment.entity.User;
import com.whut.dbexperiment.entity.UserDto;
import com.whut.dbexperiment.mapper.UserMapper;
import com.whut.dbexperiment.service.ProjService;
import com.whut.dbexperiment.service.ProjUserService;
import com.whut.dbexperiment.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private ProjUserService projUserService;

    @Resource
    private ProjService projService;

    /**
     * 获取所有人员Dto对象对应的Page对象
     *
     * @param pageAns 最终的Page对象
     */
    @Transactional
    @Override
    public void getUserDtos(Page pageAns) {

        //创建条件构造器
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();

        //添加查询条件
        userQueryWrapper.ne(User::getUsername, "admin");

        //查询除了admin之外的User集合
        List<User> userList = super.list(userQueryWrapper);

        //再对proj_user表进行查询，最终生成UserDto集合
        List<UserDto> userDtoList = userList.stream().map(user -> {

            //获取单个User对应的id
            Long userId = user.getId();

            //再对proj_user表进行查询
            LambdaQueryWrapper<ProjUser> projUserQueryWrapper = new LambdaQueryWrapper<>();
            projUserQueryWrapper.eq(ProjUser::getUserId, userId);
            List<ProjUser> projUserList = projUserService.list(projUserQueryWrapper);

            //获取projs属性
            List<String> projs = projUserList.stream().map(projUser -> {

                //对于每一个ProjUser类对象，先得到projId
                Long projId = projUser.getProjId();

                //查询proj表，根据projId得到项目名称并返回
                LambdaQueryWrapper<Proj> projQueryWrapper = new LambdaQueryWrapper<>();
                projQueryWrapper.eq(Proj::getId, projId);
                Proj proj = projService.getOne(projQueryWrapper);
                return proj.getProjName();
            }).collect(Collectors.toList());

            //最后返回UserDto类对象
            UserDto userDto = new UserDto();
            userDto.setUsername(user.getUsername());
            userDto.setUsersex(user.getUsersex());
            userDto.setProjs(projs);
            return userDto;
        }).collect(Collectors.toList());

        //设置pageAns的相关属性
        pageAns.setTotal(userList.size());
        pageAns.setRecords(userDtoList);
    }
}
