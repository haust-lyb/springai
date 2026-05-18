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
public class Update_01_InitDefaultUser implements UpdateRunner {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run() {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(BCrypt.hashpw("admin"));
            admin.setNickname("管理员");
            admin.setRole("admin");
            admin.setStatus("enabled");
            userRepository.save(admin);
            log.info("默认管理员用户初始化完成");
        } else {
            log.info("用户已存在，跳过默认用户初始化");
        }
    }
}
