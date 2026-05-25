package com.hsr.springai.controller;

import com.hsr.springai.dto.LoginRequest;
import com.hsr.springai.dto.Result;
import com.hsr.springai.entity.User;
import com.hsr.springai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/api/auth/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest request) {
        return Result.success(userService.login(request));
    }

    @PostMapping("/api/auth/logout")
    public Result<Void> logout() {
        userService.logout();
        return Result.success();
    }

    @GetMapping("/api/auth/userinfo")
    public Result<Map<String, Object>> getUserInfo() {
        User user = userService.getCurrentUser();
        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("role", user.getRole());
        return Result.success(result);
    }
}
