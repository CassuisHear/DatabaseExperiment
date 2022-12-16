package com.whut.dbexperiment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.dbexperiment.entity.Proj;
import com.whut.dbexperiment.entity.ProjUser;
import com.whut.dbexperiment.mapper.ProjMapper;
import com.whut.dbexperiment.service.ProjService;
import com.whut.dbexperiment.service.ProjUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProjServiceImpl extends ServiceImpl<ProjMapper, Proj> implements ProjService {

    @Resource
    private ProjUserService projUserService;

    //当项目状态projStatus存在于newProjStatus中时，
    //需要在proj表中修改endTime这一属性
    private static final Set<String> newProjStatus;

    static {
        newProjStatus = new HashSet<>();
        newProjStatus.add("failed");
        newProjStatus.add("finished");
    }

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
     * 修改一个项目，
     * 注意Proj表和ProjUser表都需要更改
     *
     * @param proj 封装的Proj类对象
     */
    @Transactional
    @Override
    public void updateOneProj(Proj proj) {

        String projStatus = proj.getProjStatus();
        if (newProjStatus.contains(projStatus)) {
            //设置这个项目的endTime
            proj.setEndTime(LocalDateTime.now());
        }

        //调用原始方法在Proj表中更新该Proj类对象
        super.updateById(proj);

        //更新proj_user表中的相关信息
        Long projId = proj.getId();
        String newProjProgress = projToProjUser.get(projStatus);
        LambdaQueryWrapper<ProjUser> projUserQueryWrapper = new LambdaQueryWrapper<>();
        projUserQueryWrapper.eq(ProjUser::getProjId, projId);
        List<ProjUser> projUserList = projUserService.list(projUserQueryWrapper);
        for (ProjUser projUser : projUserList) {
            projUser.setProjProgress(newProjProgress);
            UpdateWrapper<ProjUser> projUserUpdateWrapper = new UpdateWrapper<>();
            projUserUpdateWrapper.eq("proj_id", projUser.getProjId())
                    .eq("user_id", projUser.getUserId());
            projUserService.update(projUser, projUserUpdateWrapper);
        }
    }

    /**
     * 根据项目id删除一个项目，
     * 注意Proj表和ProjUser表都需要更改
     *
     * @param id 项目的id
     */
    @Transactional
    @Override
    public void removeOneProj(Long id) {

        //1.删除proj表中该项目的记录
        super.removeById(id);

        //2.删除proj_user表中的相关记录
        LambdaQueryWrapper<ProjUser> projUserQueryWrapper = new LambdaQueryWrapper<>();
        projUserQueryWrapper.eq(ProjUser::getProjId, id);
        projUserService.remove(projUserQueryWrapper);
    }
}
