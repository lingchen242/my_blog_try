package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String role; // admin / user

    private String nickname;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
