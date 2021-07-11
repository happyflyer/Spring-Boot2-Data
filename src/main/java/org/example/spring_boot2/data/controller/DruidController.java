package org.example.spring_boot2.data.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author lifei
 */
@Slf4j
@Controller
public class DruidController {
    @Resource
    JdbcTemplate jdbcTemplate;

    @ResponseBody
    @GetMapping("get_account")
    public List<Map<String, Object>> getAccount() {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(
                "select user_id, money from account_tbl;");
        log.info("查询到的数据 {}", maps);
        return maps;
    }
}
