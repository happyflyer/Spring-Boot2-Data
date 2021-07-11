package org.example.spring_boot2.data.mapper;

// import org.apache.ibatis.annotations.Mapper;
import org.example.spring_boot2.data.bean.Account;

/**
 * @author lifei
 */
// @Mapper
public interface AccountMapper {
    /**
     * getAccount
     *
     * @param id id
     * @return Account
     */
    Account getAccount(Integer id);
}
