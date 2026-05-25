package com.hsr.springai.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpInterface;
import com.hsr.springai.entity.User;
import com.hsr.springai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SaTokenConfig implements WebMvcConfigurer, StpInterface {

    private final UserRepository userRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor())
                .addPathPatterns("/api/**")
//                .excludePathPatterns("/test/**")
                .excludePathPatterns("/api/auth/login");
    }

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return new ArrayList<>();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return new ArrayList<>();
    }
}
