package com.example.demo.controller;

import com.example.demo.common.JwtUtil;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.encoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        if (username == null || password == null) {
            return Result.error(400, "用户名和密码不能为空");
        }

        User user = userService.findByUsername(username);
        if (user == null || !encoder.matches(password, user.getPassword())) {
            return Result.error(401, "用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        return Result.success(Map.of(
                "token", token,
                "role", user.getRole(),
                "nickname", user.getNickname()
        ));
    }

    @PostMapping("/guest-login")
    public Result<Map<String, Object>> guestLogin() {
        String guestName = "游客_" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        String token = jwtUtil.generateToken(0L, guestName, "guest");
        return Result.success(Map.of(
                "token", token,
                "role", "guest",
                "nickname", guestName
        ));
    }
}