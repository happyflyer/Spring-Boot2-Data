package org.example.spring_boot2.data;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Slf4j
@SpringBootTest
class DruidDataSourceTest {
    @Resource
    DataSource dataSource;

    @Test
    void testDruidDataSource() {
        log.info("dataSource 的类型是 {}", dataSource.getClass().getName());
    }
}
