package org.example.spring_boot2.data.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.spring_boot2.data.bean.Account;
import org.example.spring_boot2.data.bean.City;
import org.example.spring_boot2.data.service.AccountService;
import org.example.spring_boot2.data.service.CityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author lifei
 */
@Slf4j
@Controller
@RequestMapping("/mybatis")
public class MybatisController {
    @Resource
    AccountService accountService;

    @Resource
    CityService cityService;

    @ResponseBody
    @GetMapping("test1")
    public Account getAccount(@RequestParam("id") Integer id) {
        Account account = accountService.getAccountById(id);
        log.info("查询到的数据 {}", account);
        return account;
    }

    @ResponseBody
    @GetMapping("test2")
    public City getCity(@RequestParam("id") Integer id) {
        City city = cityService.getCityById(id);
        log.info("查询到的数据 {}", city);
        return city;
    }

    @ResponseBody
    @GetMapping("test3")
    public City getCity(City city) {
        cityService.insertCity(city);
        log.info("插入的数据 {}", city);
        return city;
    }
}
