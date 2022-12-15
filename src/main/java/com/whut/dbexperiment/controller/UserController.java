package com.whut.dbexperiment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.dbexperiment.common.R;
import com.whut.dbexperiment.entity.User;
import com.whut.dbexperiment.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户/人员 登录
     *
     * @param request 原生的 Servlet 请求
     * @param user    封装的 User 类对象
     * @return 登录成功后 用户/人员 的信息
     */
    @PostMapping("/login")
    public R<User> userLogin(HttpServletRequest request,
                             @RequestBody User user) {

        //1、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User userFromDB = userService.getOne(queryWrapper);

        //2、如果没有查询到则返回登录失败的结果
        if (userFromDB == null) {
            return R.error("登录失败...");
        }

        //3、密码比对，如果不一致则返回登录失败的结果
        String password = user.getPassword();
        if (!password.equals(userFromDB.getPassword())) {
            return R.error("登录失败...");
        }

        //4、登录成功，将 用户/人员 id存入Session并返回登录成功结果
        request.getSession().setAttribute("user", userFromDB.getId());

        return R.success(userFromDB);
    }

    /**
     * 用户/人员 登出
     *
     * @param request 原生的 Servlet 请求
     * @return 登出成功的信息
     */
    @PostMapping("/logout")
    public R<String> userLogout(HttpServletRequest request) {
        //清除Session中保存的当前登录 用户/人员 的id
        request.getSession().removeAttribute("user");
        return R.success("退出成功!");
    }

    /**
     * 人员的分页查询
     *
     * @param page     当前页的页码
     * @param pageSize 分页的大小
     * @return 分页的结果
     */
    @GetMapping("/page")
    public R<Page> pageUser(int page, int pageSize) {

        //创建分页构造器
        Page pageAns = new Page(page, pageSize);

        //调用拓展的方法进行查询
        userService.getUserDtos(pageAns);

        return R.success(pageAns);
    }

    /**
     * 添加人员
     *
     * @param user 封装的User类对象
     * @return 添加成功的信息
     */
    @PostMapping
    public R<String> addUser(@RequestBody User user) {

        //默认新添加的人员初始密码都为123456
        user.setPassword("123456");

        //直接调用方法添加人员信息
        userService.save(user);

        return R.success("添加人员成功!");
    }


}
