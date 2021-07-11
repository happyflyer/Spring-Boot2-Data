package org.example.spring_boot2.data.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lifei
 */
@Slf4j
@Component
public class UrlCountInterceptor implements HandlerInterceptor {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String uri = request.getRequestURI();
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.increment(uri);
        log.info("获取到的数据 {}: {}", uri, operations.get(uri));
        return true;
    }
}
