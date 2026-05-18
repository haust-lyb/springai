package com.hsr.springai.service;

import com.hsr.springai.entity.Model;
import com.hsr.springai.entity.Provider;
import com.hsr.springai.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelRepository modelRepository;

    @Transactional(readOnly = true)
    public List<Model> findByProviderId(Long providerId) {
        return modelRepository.findByProviderId(providerId);
    }

    @Transactional(readOnly = true)
    public List<Model> findAllEnabledModels() {
        return modelRepository.findByProvider_Status("enabled");
    }

    @Transactional
    public void syncModels(Provider provider, List<String> modelNames) {
        modelRepository.deleteByProviderId(provider.getId());
        for (String name : modelNames) {
            Model model = new Model();
            model.setName(name);
            model.setProvider(provider);
            modelRepository.save(model);
        }
        log.info("Provider [{}] 模型同步完成，共 {} 个模型", provider.getName(), modelNames.size());
    }

    @Transactional
    public void deleteByProviderId(Long providerId) {
        modelRepository.deleteByProviderId(providerId);
    }
}
