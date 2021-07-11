package org.example.spring_boot2.data.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.spring_boot2.data.bean.User;
import org.example.spring_boot2.data.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lifei
 */
@Slf4j
@Controller
@RequestMapping("/mybatis_plus")
public class MybatisPlusController {
    @Resource
    UserService userServiceImpl;

    @ResponseBody
    @GetMapping("/list")
    public List<User> listUsers() {
        List<User> list = userServiceImpl.list();
        log.info("查询到的数据 {}", list);
        return list;
    }

    @ResponseBody
    @GetMapping("/page")
    public Page<User> pageUsers(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
        Page<User> userPage = new Page<>(pn, 2);
        Page<User> page = userServiceImpl.page(userPage, null);
        log.info("查询到的数据 {}", page.getRecords());
        return page;
    }

    @ResponseBody
    @GetMapping("/delete/{id}")
    public User deleteUser(@PathVariable("id") Integer id) {
        User user = userServiceImpl.getById(id);
        log.info("要删除的数据 {}", user);
        if (user != null && userServiceImpl.removeById(id)) {
            return user;
        }
        return null;
    }
}
