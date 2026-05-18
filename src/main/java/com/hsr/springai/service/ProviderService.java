package com.hsr.springai.service;

import com.hsr.springai.entity.Provider;
import com.hsr.springai.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ModelService modelService;

    public List<Provider> findAll() {
        return providerRepository.findAll();
    }

    public Provider findById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("提供商不存在: " + id));
    }

    @Transactional
    public Provider create(String name, String type, String apiKey, String baseUrl, String logo, String iconColor) {
        if (providerRepository.existsByName(name)) {
            throw new IllegalArgumentException("提供商名称已存在: " + name);
        }
        Provider provider = new Provider();
        provider.setName(name);
        provider.setType(type);
        provider.setApiKey(apiKey);
        provider.setBaseUrl(baseUrl);
        provider.setLogo(logo);
        if (iconColor != null) {
            provider.setIconColor(iconColor);
        }
        provider.setStatus("enabled");
        provider.setIsBuiltin(false);
        return providerRepository.save(provider);
    }

    @Transactional
    public Provider update(Long id, String name, String type, String apiKey, String baseUrl,
                           String status, String logo, String iconColor, String modelsJson) {
        Provider provider = findById(id);
        if (name != null) provider.setName(name);
        if (type != null) provider.setType(type);
        if (apiKey != null) provider.setApiKey(apiKey);
        if (baseUrl != null) provider.setBaseUrl(baseUrl);
        if (status != null) provider.setStatus(status);
        if (logo != null) provider.setLogo(logo);
        if (iconColor != null) provider.setIconColor(iconColor);
        return providerRepository.save(provider);
    }

    @Transactional
    public void delete(Long id) {
        modelService.deleteByProviderId(id);
        providerRepository.deleteById(id);
    }

    @Transactional
    public List<Map<String, String>> fetchOllamaModels(Long providerId) {
        Provider provider = findById(providerId);
        if (!"ollama".equalsIgnoreCase(provider.getType())) {
            throw new IllegalArgumentException("仅支持 Ollama 类型的提供商获取模型列表");
        }
        String baseUrl = provider.getBaseUrl();
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("请先配置 API 地址");
        }

        log.info("正在从 Ollama 获取模型列表: baseUrl={}", baseUrl);
        var api = OllamaApi.builder()
                .baseUrl(baseUrl)
                .build();

        var response = api.listModels();
        List<String> modelNames = new ArrayList<>();

        if (response != null && response.models() != null) {
            for (var model : response.models()) {
                String name = model.name() != null ? model.name() : model.model();
                modelNames.add(name);
            }
        }

        modelService.syncModels(provider, modelNames);

        List<Map<String, String>> result = new ArrayList<>();
        for (String name : modelNames) {
            Map<String, String> item = new HashMap<>();
            item.put("name", name);
            item.put("group", provider.getName());
            result.add(item);
        }
        return result;
    }
}
