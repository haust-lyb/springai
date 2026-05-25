package com.hsr.springai.service;

import cn.hutool.crypto.digest.BCrypt;
import com.hsr.springai.dto.UserStats;
import com.hsr.springai.dto.page.PageQueryRequest;
import com.hsr.springai.dto.page.PageResult;
import com.hsr.springai.entity.User;
import com.hsr.springai.repository.UserRepository;
import com.hsr.springai.support.query.JpaPageQueryHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private static final String ROLE_ADMIN = "admin";
    private static final String ROLE_USER = "user";
    private static final String STATUS_ENABLED = "enabled";
    private static final String STATUS_DISABLED = "disabled";
    private static final Map<String, String> QUERY_FIELDS = Map.of(
            "id", "id",
            "username", "username",
            "nickname", "nickname",
            "email", "email",
            "role", "role",
            "status", "status",
            "isBuiltin", "isBuiltin",
            "createTime", "createTime",
            "updateTime", "updateTime"
    );

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
     * 分页多条件查询用户（仅管理员）
     */
    public PageResult<User> pageUsers(PageQueryRequest request) {
        checkAdmin();
        if (request == null) {
            request = new PageQueryRequest();
        }
        Specification<User> specification = JpaPageQueryHelper.specification(request, QUERY_FIELDS);
        Page<User> page = userRepository.findAll(
                specification,
                JpaPageQueryHelper.pageable(request, QUERY_FIELDS, "createTime")
        );
        return PageResult.from(page);
    }

    /**
     * 获取用户统计信息（仅管理员）
     */
    public UserStats getStats() {
        checkAdmin();
        long total = userRepository.count();
        long enabled = userRepository.count(JpaPageQueryHelper.equal("status", STATUS_ENABLED));
        long disabled = userRepository.count(JpaPageQueryHelper.equal("status", STATUS_DISABLED));
        long admin = userRepository.count(JpaPageQueryHelper.equal("role", ROLE_ADMIN));
        long builtin = userRepository.count(JpaPageQueryHelper.equal("isBuiltin", true));
        return new UserStats(total, enabled, disabled, admin, builtin);
    }

    /**
     * 获取用户详情（仅管理员）
     */
    public User getUser(Long userId) {
        checkAdmin();
        return findUser(userId);
    }

    /**
     * 创建新用户（仅管理员）
     */
    @Transactional
    public User createUser(String username, String password, String nickname, String email, String role, String status) {
        checkAdmin();

        username = normalizeRequired(username, "用户名不能为空");
        password = normalizeRequired(password, "密码不能为空");
        role = normalizeRole(role);
        status = normalizeStatus(status);

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(password));
        user.setNickname(hasText(nickname) ? nickname.trim() : username);
        user.setEmail(hasText(email) ? email.trim() : null);
        user.setRole(role);
        user.setStatus(status);
        user.setIsBuiltin(false);

        return userRepository.save(user);
    }

    /**
     * 更新用户资料（仅管理员）
     */
    @Transactional
    public User updateUser(Long userId, String username, String nickname, String email, String role, String status) {
        checkAdmin();

        User user = findUser(userId);

        if (hasText(username)) {
            String normalizedUsername = username.trim();
            userRepository.findByUsername(normalizedUsername)
                    .filter(existing -> !existing.getId().equals(userId))
                    .ifPresent(existing -> {
                        throw new RuntimeException("用户名已存在");
                    });
            user.setUsername(normalizedUsername);
        }

        if (nickname != null) {
            user.setNickname(hasText(nickname) ? nickname.trim() : user.getUsername());
        }
        if (email != null) {
            user.setEmail(hasText(email) ? email.trim() : null);
        }
        if (role != null) {
            ensureBuiltinAdminRoleCanChange(user);
            user.setRole(normalizeRole(role));
        }
        if (status != null) {
            user.setStatus(normalizeStatus(status));
        }

        return userRepository.save(user);
    }

    /**
     * 修改用户角色（仅管理员）
     */
    @Transactional
    public User updateRole(Long userId, String role) {
        checkAdmin();

        User user = findUser(userId);

        ensureBuiltinAdminRoleCanChange(user);

        user.setRole(normalizeRole(role));
        return userRepository.save(user);
    }

    /**
     * 重置用户密码（仅管理员）
     */
    @Transactional
    public void resetPassword(Long userId, String newPassword) {
        checkAdmin();

        newPassword = normalizeRequired(newPassword, "密码不能为空");

        User user = findUser(userId);

        user.setPassword(BCrypt.hashpw(newPassword));
        userRepository.save(user);
    }

    /**
     * 删除用户（仅管理员）
     */
    @Transactional
    public void deleteUser(Long userId) {
        checkAdmin();

        User user = findUser(userId);

        if (Boolean.TRUE.equals(user.getIsBuiltin())) {
            throw new RuntimeException("内建用户不能删除");
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

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    private void ensureBuiltinAdminRoleCanChange(User user) {
        if (Boolean.TRUE.equals(user.getIsBuiltin()) && ROLE_ADMIN.equals(user.getRole())) {
            throw new RuntimeException("内建管理员角色不能修改");
        }
    }

    private String normalizeRole(String role) {
        if (!hasText(role)) {
            return ROLE_USER;
        }
        String value = role.trim();
        if (!ROLE_ADMIN.equals(value) && !ROLE_USER.equals(value)) {
            throw new RuntimeException("角色只能是 admin 或 user");
        }
        return value;
    }

    private String normalizeStatus(String status) {
        if (!hasText(status)) {
            return STATUS_ENABLED;
        }
        String value = status.trim();
        if (!STATUS_ENABLED.equals(value) && !STATUS_DISABLED.equals(value)) {
            throw new RuntimeException("状态只能是 enabled 或 disabled");
        }
        return value;
    }

    private String normalizeRequired(String value, String message) {
        if (!hasText(value)) {
            throw new RuntimeException(message);
        }
        return value.trim();
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
