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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private ProjUserService projUserService;

    @Resource
    private ProjService projService;

    //这是proj表中proj_status属性和proj_user表中proj_progress属性的映射
    private static final Map<String, String> projToProjUser;

    static {
        projToProjUser = new HashMap<>();
        projToProjUser.put("unfinished", "未完成");
        projToProjUser.put("doing", "正在处理");
        projToProjUser.put("finished", "已完成");
        projToProjUser.put("failed", "失败");
    }

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
        userQueryWrapper.orderByDesc(User::getId);

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
            userDto.setPassword(user.getPassword());
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setUsersex(user.getUsersex());
            userDto.setProjs(projs);
            return userDto;
        }).collect(Collectors.toList());

        //设置pageAns的相关属性
        pageAns.setTotal(userList.size());
        pageAns.setRecords(userDtoList);
    }

    /**
     * 根据传回来的UserDto对象更新某个人员所属的项目
     *
     * @param userDto 封装的UserDto对象
     */
    @Transactional
    @Override
    public void updateProjUserByUserDto(UserDto userDto) {

        //1.获取该人员的id和修改后的项目名称集合
        Long userId = userDto.getId();
        List<String> newProjNames = userDto.getProjs();

        //2.查询proj_user表，获取该人员原本所属项目的id集合
        LambdaQueryWrapper<ProjUser> projuserQueryWrapper = new LambdaQueryWrapper<>();
        projuserQueryWrapper.eq(ProjUser::getUserId, userId);
        List<ProjUser> projUserList = projUserService.list(projuserQueryWrapper);
        List<Long> oldProjIds = projUserList.stream().map(ProjUser::getProjId).collect(Collectors.toList());

        //3.更新proj表，将该人员原本所属的各个项目的user_count属性-1
        for (Long oldProjId : oldProjIds) {
            LambdaQueryWrapper<Proj> projQueryWrapper = new LambdaQueryWrapper<>();
            projQueryWrapper.eq(Proj::getId, oldProjId);
            Proj oldProj = projService.getOne(projQueryWrapper);
            oldProj.setUserCount(oldProj.getUserCount() - 1);
            projService.updateById(oldProj);
        }

        //4.更新proj_user表，将该人员原本的相关记录删除
        projUserService.remove(projuserQueryWrapper);

        for (String newProjName : newProjNames) {
            //5.更新proj表，将所有修改后的项目的user_count属性+1
            LambdaQueryWrapper<Proj> projQueryWrapper2 = new LambdaQueryWrapper<>();
            projQueryWrapper2.eq(Proj::getProjName, newProjName);
            Proj newProj = projService.getOne(projQueryWrapper2);
            newProj.setUserCount(newProj.getUserCount() + 1);
            projService.updateById(newProj);

            //6.更新proj_user表，添加修改后的项目和该人员之间的记录
            Long projId = newProj.getId();
            ProjUser newProjUser = new ProjUser();
            newProjUser.setProjId(projId);
            newProjUser.setUserId(userId);
            newProjUser.setResPart(userId + "号人员在负责项目" + projId);
            String newProjProgress = projToProjUser.get(newProj.getProjStatus());
            newProjUser.setProjProgress(newProjProgress);
            projUserService.save(newProjUser);
        }
    }

    /**
     * 根据人员的id删除和这个人员相关的所有信息，
     * 包括user表、proj_user表和proj表中的所有关联属性
     *
     * @param id 传入的用户id
     */
    @Transactional
    @Override
    public void removeUserInfo(Long id) {

        //1.删除user表中该人员的记录
        super.removeById(id);

        //2.查询proj_user表，获取该人员所属多个项目的id
        LambdaQueryWrapper<ProjUser> projUserQueryWrapper = new LambdaQueryWrapper<>();
        projUserQueryWrapper.eq(ProjUser::getUserId, id);
        List<ProjUser> projUserList = projUserService.list(projUserQueryWrapper);
        List<Long> projIds = projUserList.stream().map(ProjUser::getProjId).collect(Collectors.toList());

        //3.删除proj_user表中该人员相关的记录
        projUserService.remove(projUserQueryWrapper);

        //4.根据步骤2查询出来的id更新proj表中的user_count这一列，使其数值-1
        for (Long projId : projIds) {
            Proj proj = projService.getById(projId);
            proj.setUserCount(proj.getUserCount() - 1);
            projService.updateById(proj);
        }
    }
}
