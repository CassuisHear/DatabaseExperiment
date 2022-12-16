package com.whut.dbexperiment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.dbexperiment.common.R;
import com.whut.dbexperiment.entity.Proj;
import com.whut.dbexperiment.entity.User;
import com.whut.dbexperiment.entity.UserDto;
import com.whut.dbexperiment.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

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

        //直接调用方法添加人员信息
        userService.save(user);

        return R.success("添加人员成功!");
    }

    /**
     * 修改人员
     *
     * @param user 封装的User类对象
     * @return 修改成功的信息
     */
    @PutMapping
    public R<String> updateUser(@RequestBody User user) {

        //直接调用方法修改人员信息
        userService.updateById(user);

        return R.success("人员修改成功!");
    }

    /**
     * 删除人员
     *
     * @param ids 封装的User类对象
     * @return 删除成功的信息
     */
    @DeleteMapping
    public R<String> deleteUser(@RequestParam Long ids) {

        //直接调用拓展的方法删除该人员信息
        userService.removeUserInfo(ids);

        return R.success("人员删除成功！");
    }

    /**
     * 根据传回来的UserDto对象更新某个人员所属的项目
     *
     * @param userDto 封装的UserDto对象
     * @return 更新成功的信息
     */
    @PutMapping("/userDto")
    public R<String> updateUserDto(@RequestBody UserDto userDto) {

        //直接调用拓展的方法进行相关信息的更新
        userService.updateProjUserByUserDto(userDto);

        return R.success("该人员所属的项目已修改成功!");
    }
}
