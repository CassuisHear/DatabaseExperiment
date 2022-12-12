package com.whut.dbexperiment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.dbexperiment.entity.ProjUser;
import com.whut.dbexperiment.mapper.ProjUserMapper;
import com.whut.dbexperiment.service.ProjUserService;
import org.springframework.stereotype.Service;

@Service
public class ProjUserServiceImpl extends ServiceImpl<ProjUserMapper, ProjUser> implements ProjUserService {
}
