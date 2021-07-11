package org.example.spring_boot2.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.spring_boot2.data.bean.User;
import org.example.spring_boot2.data.mapper.UserMapper;
import org.example.spring_boot2.data.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author lifei
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
