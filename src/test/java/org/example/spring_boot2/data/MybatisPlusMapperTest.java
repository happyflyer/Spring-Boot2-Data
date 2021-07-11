package org.example.spring_boot2.data;

import lombok.extern.slf4j.Slf4j;
import org.example.spring_boot2.data.bean.User;
import org.example.spring_boot2.data.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
class MybatisPlusMapperTest {
    @Resource
    UserMapper userMapper;

    @Test
    void testUserMapper() {
        User user = userMapper.selectById(1L);
        log.info("查询到的数据 {}", user);
    }
}
