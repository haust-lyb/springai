package com.hsr.springai.controller;

import com.hsr.springai.dto.Result;
import com.hsr.springai.entity.Model;
import com.hsr.springai.entity.SystemSetting;
import com.hsr.springai.repository.SystemSettingRepository;
import com.hsr.springai.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 首页模型查询接口
 * 普通用户使用，只提供查询功能
 */
@RestController
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;
    private final SystemSettingRepository systemSettingRepository;

    /**
     * 获取所有启用的模型列表（首页使用）
     */
    @GetMapping("/api/models")
    public Result<List<Map<String, Object>>> listModels() {
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

    /**
     * 获取系统默认模型（首页使用）
     */
    @GetMapping("/api/models/default")
    public Result<Map<String, Object>> getDefaultModel() {
        Optional<SystemSetting> setting = systemSettingRepository.findByConfigKey("default_model_id");
        Map<String, Object> result = new HashMap<>();
        result.put("defaultModelId", setting.map(SystemSetting::getConfigValue).orElse(null));
        return Result.success(result);
    }
}
