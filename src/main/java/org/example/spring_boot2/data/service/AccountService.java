package org.example.spring_boot2.data.service;

import org.example.spring_boot2.data.bean.Account;
import org.example.spring_boot2.data.mapper.AccountMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lifei
 */
@Service
public class AccountService {
    @Resource
    AccountMapper accountMapper;

    public Account getAccountById(Integer id) {
        return accountMapper.getAccount(id);
    }
}
