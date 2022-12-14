package com.whut.dbexperiment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.dbexperiment.common.R;
import com.whut.dbexperiment.entity.Proj;
import com.whut.dbexperiment.service.ProjService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/proj")
public class ProjController {

    @Resource
    private ProjService projService;

    /**
     * 项目的分页查询
     *
     * @param page     当前页的页码
     * @param pageSize 分页的大小
     * @return 分页的结果
     */
    @GetMapping("/page")
    public R<Page> pageProj(int page, int pageSize) {

        //创建分页构造器
        Page pageAns = new Page(page, pageSize);

        //创建条件构造器
        LambdaQueryWrapper<Proj> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        //添加查询条件
        lambdaQueryWrapper.orderByDesc(Proj::getBeginTime);

        //执行查询
        projService.page(pageAns, lambdaQueryWrapper);

        return R.success(pageAns);
    }
}
