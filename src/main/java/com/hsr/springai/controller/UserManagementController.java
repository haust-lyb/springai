package com.hsr.springai.controller;

import com.hsr.springai.dto.Result;
import com.hsr.springai.entity.User;
import com.hsr.springai.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserManagementService userManagementService;

    @GetMapping
    public Result<List<User>> getAllUsers() {
        return Result.success(userManagementService.getAllUsers());
    }

    @PostMapping
    public Result<User> createUser(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        String nickname = (String) body.get("nickname");
        String role = (String) body.get("role");

        if (username == null || username.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }

        User user = userManagementService.createUser(
                username.trim(),
                password.trim(),
                nickname,
                role
        );
        return Result.success(user);
    }

    @PostMapping("/{id}/role")
    public Result<User> updateRole(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String role = (String) body.get("role");
        if (role == null || role.trim().isEmpty()) {
            return Result.error("角色不能为空");
        }
        User user = userManagementService.updateRole(id, role.trim());
        return Result.success(user);
    }

    @PostMapping("/{id}/password")
    public Result<Void> resetPassword(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String password = (String) body.get("password");
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        userManagementService.resetPassword(id, password.trim());
        return Result.success();
    }

    @PostMapping("/{id}/delete")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userManagementService.deleteUser(id);
        return Result.success();
    }
}
