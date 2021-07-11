package org.example.spring_boot2.data.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author lifei
 */
@Data
@TableName("user_tbl")
public class User {
    private Integer id;
    private String name;
    private Integer age;
    private String email;
    @TableField(exist = false)
    private String address;
}
