package com.hsr.springai.service;

import cn.hutool.crypto.digest.BCrypt;
import com.hsr.springai.entity.User;
import com.hsr.springai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * 获取所有用户（仅管理员）
     */
    public List<User> getAllUsers() {
        checkAdmin();
        return userRepository.findAll();
    }

    /**
     * 创建新用户（仅管理员）
     */
    @Transactional
    public User createUser(String username, String password, String nickname, String role) {
        checkAdmin();

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(password));
        user.setNickname(nickname != null ? nickname : username);
        user.setRole(role != null ? role : "user");
        user.setStatus("enabled");

        return userRepository.save(user);
    }

    /**
     * 修改用户角色（仅管理员）
     */
    @Transactional
    public User updateRole(Long userId, String role) {
        checkAdmin();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // admin 用户不能修改角色
        if ("admin".equals(user.getUsername())) {
            throw new RuntimeException("管理员角色不能修改");
        }

        user.setRole(role);
        return userRepository.save(user);
    }

    /**
     * 重置用户密码（仅管理员）
     */
    @Transactional
    public void resetPassword(Long userId, String newPassword) {
        checkAdmin();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setPassword(BCrypt.hashpw(newPassword));
        userRepository.save(user);
    }

    /**
     * 删除用户（仅管理员）
     */
    @Transactional
    public void deleteUser(Long userId) {
        checkAdmin();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // admin 用户不能删除
        if ("admin".equals(user.getUsername())) {
            throw new RuntimeException("管理员账号不能删除");
        }

        userRepository.deleteById(userId);
    }

    /**
     * 检查当前用户是否是管理员
     */
    private void checkAdmin() {
        if (!userService.isAdmin()) {
            throw new RuntimeException("无权执行此操作，需要管理员权限");
        }
    }
}
