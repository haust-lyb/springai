package com.hsr.springai.controller;

import com.hsr.springai.dto.Result;
import com.hsr.springai.entity.Model;
import com.hsr.springai.entity.SystemSetting;
import com.hsr.springai.repository.SystemSettingRepository;
import com.hsr.springai.service.ModelService;
import com.hsr.springai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 设置页面模型管理接口
 * 管理员使用，包含查询和设置功能
 */
@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final ModelService modelService;
    private final SystemSettingRepository systemSettingRepository;
    private final UserService userService;

    @GetMapping("/default-model")
    public Result<Map<String, Object>> getDefaultModel() {
        checkAdmin();
        Optional<SystemSetting> setting = systemSettingRepository.findByConfigKey("default_model_id");
        Map<String, Object> result = new HashMap<>();
        result.put("defaultModelId", setting.map(SystemSetting::getConfigValue).orElse(null));
        return Result.success(result);
    }

    @PostMapping("/default-model")
    public Result<Void> setDefaultModel(@RequestBody Map<String, Object> body) {
        checkAdmin();
        String modelId = body.get("defaultModelId") != null ? body.get("defaultModelId").toString() : null;
        SystemSetting setting = systemSettingRepository.findByConfigKey("default_model_id")
                .orElse(new SystemSetting());
        setting.setConfigKey("default_model_id");
        setting.setConfigValue(modelId);
        systemSettingRepository.save(setting);
        return Result.success();
    }

    @GetMapping("/models")
    public Result<List<Map<String, Object>>> listModels() {
        checkAdmin();
        List<Model> models = modelService.findAllEnabledModels();
        List<Map<String, Object>> result = models.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", m.getId());
            map.put("name", m.getName());
            map.put("providerId", m.getProvider().getId());
            map.put("providerName", m.getProvider().getName());
            return map;
        }).collect(Collectors.toList());
        return Result.success(result);
    }

    private void checkAdmin() {
        if (!userService.isAdmin()) {
            throw new RuntimeException("无权访问此功能，需要管理员权限");
        }
    }
}
