package com.whut.dbexperiment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.dbexperiment.common.R;
import com.whut.dbexperiment.entity.Proj;
import com.whut.dbexperiment.service.ProjService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 修改项目信息
     *
     * @param proj 封装的项目对象
     * @return 项目修改成功的信息
     */
    @PutMapping
    public R<String> updateProj(@RequestBody Proj proj) {

        //直接调用方法进行更新
        projService.updateById(proj);

        return R.success("项目修改成功!");
    }

    /**
     * 新增项目
     *
     * @param proj 封装的项目对象，初始情况下就只有projName一个属性
     * @return 添加项目成功的信息
     */
    @PostMapping
    public R<String> addProj(@RequestBody Proj proj) {

        //为这个对象的其他属性赋值并添加到数据库中
        proj.setUserCount(0);
        proj.setProjStatus("unfinished");
        projService.save(proj);

        return R.success("项目添加成功!");
    }

    /**
     * 删除项目
     *
     * @param ids 接收的项目id值
     * @return 删除成功的信息
     */
    @DeleteMapping
    public R<String> deleteProj(@RequestParam Long ids) {

        //直接调用方法将改项目删除即可
        projService.removeById(ids);

        return R.success("项目删除成功!");
    }
}
