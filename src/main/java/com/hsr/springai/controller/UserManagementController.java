package com.hsr.springai.controller;

import com.hsr.springai.dto.Result;
import com.hsr.springai.dto.UserStats;
import com.hsr.springai.dto.page.PageQueryRequest;
import com.hsr.springai.dto.page.PageResult;
import com.hsr.springai.entity.User;
import com.hsr.springai.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserManagementController {

    private final UserManagementService userManagementService;

    @GetMapping("/api/admin/users")
    public Result<List<User>> getAllUsers() {
        return Result.success(userManagementService.getAllUsers());
    }

    @PostMapping("/api/admin/users/page")
    public Result<PageResult<User>> pageUsers(@RequestBody PageQueryRequest request) {
        return Result.success(userManagementService.pageUsers(request));
    }

    @GetMapping("/api/admin/users/stats")
    public Result<UserStats> getStats() {
        return Result.success(userManagementService.getStats());
    }

    @GetMapping("/api/admin/users/detail")
    public Result<User> getUser(@RequestParam Long id) {
        return Result.success(userManagementService.getUser(id));
    }

    @PostMapping("/api/admin/users")
    public Result<User> createUser(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        String nickname = (String) body.get("nickname");
        String email = (String) body.get("email");
        String role = (String) body.get("role");
        String status = (String) body.get("status");

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
                email,
                role,
                status
        );
        return Result.success(user);
    }

    @PostMapping("/api/admin/users/update")
    public Result<User> updateUser(@RequestParam Long id, @RequestBody Map<String, Object> body) {
        String username = body.get("username") != null ? (String) body.get("username") : null;
        String nickname = body.get("nickname") != null ? (String) body.get("nickname") : null;
        String email = body.get("email") != null ? (String) body.get("email") : null;
        String role = body.get("role") != null ? (String) body.get("role") : null;
        String status = body.get("status") != null ? (String) body.get("status") : null;

        User user = userManagementService.updateUser(id, username, nickname, email, role, status);
        return Result.success(user);
    }

    @PostMapping("/api/admin/users/role")
    public Result<User> updateRole(@RequestParam Long id, @RequestBody Map<String, Object> body) {
        String role = (String) body.get("role");
        if (role == null || role.trim().isEmpty()) {
            return Result.error("角色不能为空");
        }
        User user = userManagementService.updateRole(id, role.trim());
        return Result.success(user);
    }

    @PostMapping("/api/admin/users/password")
    public Result<Void> resetPassword(@RequestParam Long id, @RequestBody Map<String, Object> body) {
        String password = (String) body.get("password");
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        userManagementService.resetPassword(id, password.trim());
        return Result.success();
    }

    @PostMapping("/api/admin/users/delete")
    public Result<Void> deleteUser(@RequestParam Long id) {
        userManagementService.deleteUser(id);
        return Result.success();
    }
}
