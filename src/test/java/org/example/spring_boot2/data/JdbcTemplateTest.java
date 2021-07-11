package org.example.spring_boot2.data;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
class JdbcTemplateTest {
    @Resource
    JdbcTemplate jdbcTemplate;

    @Test
    void testJdbcTemplate() {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(
                "select user_id, money from account_tbl;");
        maps.forEach(System.out::println);
        Integer count = jdbcTemplate.queryForObject(
                "select count(id) from account_tbl;", Integer.class);
        log.info("account_tbl表中记录有 {} 条", count);
    }
}
