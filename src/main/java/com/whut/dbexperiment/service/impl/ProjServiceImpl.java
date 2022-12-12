package com.whut.dbexperiment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.dbexperiment.entity.Proj;
import com.whut.dbexperiment.mapper.ProjMapper;
import com.whut.dbexperiment.service.ProjService;
import org.springframework.stereotype.Service;

@Service
public class ProjServiceImpl extends ServiceImpl<ProjMapper, Proj> implements ProjService {
}
