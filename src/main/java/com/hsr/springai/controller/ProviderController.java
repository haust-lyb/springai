package com.hsr.springai.controller;

import com.hsr.springai.dto.Result;
import com.hsr.springai.entity.Model;
import com.hsr.springai.entity.Provider;
import com.hsr.springai.service.ModelService;
import com.hsr.springai.service.ProviderService;
import com.hsr.springai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;
    private final ModelService modelService;
    private final UserService userService;

    @GetMapping
    public Result<List<Provider>> list() {
        checkAdmin();
        return Result.success(providerService.findAll());
    }

    @PostMapping
    public Result<Provider> create(@RequestBody Map<String, Object> body) {
        checkAdmin();
        String name = (String) body.get("name");
        String type = (String) body.get("type");
        String apiKey = body.get("apiKey") != null ? (String) body.get("apiKey") : "";
        String baseUrl = body.get("baseUrl") != null ? (String) body.get("baseUrl") : "";
        String logo = body.get("logo") != null ? (String) body.get("logo") : null;
        String iconColor = body.get("iconColor") != null ? (String) body.get("iconColor") : null;

        Provider provider = providerService.create(name, type, apiKey, baseUrl, logo, iconColor);
        return Result.success(provider);
    }

    @PostMapping("/{id}/update")
    public Result<Provider> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        checkAdmin();
        String name = body.get("name") != null ? (String) body.get("name") : null;
        String type = body.get("type") != null ? (String) body.get("type") : null;
        String apiKey = body.get("apiKey") != null ? (String) body.get("apiKey") : null;
        String baseUrl = body.get("baseUrl") != null ? (String) body.get("baseUrl") : null;
        String status = body.get("status") != null ? (String) body.get("status") : null;
        String logo = body.get("logo") != null ? (String) body.get("logo") : null;
        String iconColor = body.get("iconColor") != null ? (String) body.get("iconColor") : null;
        String modelsJson = body.get("modelsJson") != null ? (String) body.get("modelsJson") : null;

        Provider provider = providerService.update(id, name, type, apiKey, baseUrl, status, logo, iconColor, modelsJson);
        return Result.success(provider);
    }

    @PostMapping("/{id}/delete")
    public Result<Void> delete(@PathVariable Long id) {
        checkAdmin();
        providerService.delete(id);
        return Result.success();
    }

    @PostMapping("/{id}/fetch-models")
    public Result<List<Map<String, String>>> fetchModels(@PathVariable Long id) {
        checkAdmin();
        List<Map<String, String>> models = providerService.fetchOllamaModels(id);
        return Result.success(models);
    }

    @GetMapping("/{id}/models")
    public Result<List<Model>> listProviderModels(@PathVariable Long id) {
        checkAdmin();
        return Result.success(modelService.findByProviderId(id));
    }

    private void checkAdmin() {
        if (!userService.isAdmin()) {
            throw new RuntimeException("无权访问此功能，需要管理员权限");
        }
    }
}
