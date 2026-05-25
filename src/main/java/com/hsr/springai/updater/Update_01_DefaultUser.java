package com.hsr.springai.updater;

import cn.hutool.crypto.digest.BCrypt;
import com.hsr.springai.entity.User;
import com.hsr.springai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class Update_01_DefaultUser implements UpdateRunner {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run() {
        User admin = userRepository.findByUsername("admin").orElse(null);
        if (admin == null && userRepository.count() == 0) {
            User defaultAdmin = new User();
            defaultAdmin.setUsername("admin");
            defaultAdmin.setPassword(BCrypt.hashpw("admin"));
            defaultAdmin.setNickname("管理员");
            defaultAdmin.setRole("admin");
            defaultAdmin.setStatus("enabled");
            defaultAdmin.setIsBuiltin(true);
            userRepository.save(defaultAdmin);
            log.info("默认管理员用户初始化完成");
        } else if (admin != null && !Boolean.TRUE.equals(admin.getIsBuiltin())) {
            admin.setIsBuiltin(true);
            userRepository.save(admin);
            log.info("默认管理员用户已标记为内建用户");
        } else {
            log.info("用户已存在，跳过默认用户初始化");
        }
    }
}
