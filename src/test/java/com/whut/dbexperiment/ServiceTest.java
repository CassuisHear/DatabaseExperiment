package com.whut.dbexperiment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whut.dbexperiment.entity.Proj;
import com.whut.dbexperiment.entity.ProjUser;
import com.whut.dbexperiment.entity.User;
import com.whut.dbexperiment.service.ProjService;
import com.whut.dbexperiment.service.ProjUserService;
import com.whut.dbexperiment.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;
import java.util.List;

@DisplayName("在Service层上对数据库的测试...")
@ComponentScan("com.whut.dbexperiment.service")
@SpringBootTest
class ServiceTest {

    @Resource
    private UserService userService;

    @Resource
    private ProjService projService;

    @Resource
    private ProjUserService projUserService;

    @Test
    @DisplayName("查询测试...")
    void testSelect() {
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Proj> projQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<ProjUser> projUserQueryWrapper = new LambdaQueryWrapper<>();

        List<User> userList = userService.list(userQueryWrapper);
        List<Proj> projList = projService.list(projQueryWrapper);
        List<ProjUser> projUserList = projUserService.list(projUserQueryWrapper);

        for (User user : userList) {
            System.out.println("user = " + user);
        }

        System.out.println("=======================");

        for (Proj proj : projList) {
            System.out.println("proj = " + proj);
        }

        System.out.println("=======================");

        for (ProjUser projUser : projUserList) {
            System.out.println("projUser = " + projUser);
        }
    }
}
