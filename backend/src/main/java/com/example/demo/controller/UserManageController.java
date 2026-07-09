package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserManageController {

    private final UserMapper userMapper;

    public UserManageController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping
    public Result<List<User>> list(HttpServletRequest request) {
        if (!"admin".equals(request.getAttribute("role"))) {
            return Result.error(403, "无权限");
        }
        return Result.success(userMapper.selectList(null));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        if (!"admin".equals(request.getAttribute("role"))) {
            return Result.error(403, "无权限");
        }
        // 不能删除自己
        Long selfId = (Long) request.getAttribute("userId");
        if (selfId.equals(id)) {
            return Result.error(400, "不能删除自己");
        }
        userMapper.deleteById(id);
        return Result.success();
    }
}