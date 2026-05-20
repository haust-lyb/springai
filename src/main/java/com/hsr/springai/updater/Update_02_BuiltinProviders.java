package com.hsr.springai.updater;

import com.hsr.springai.entity.Provider;
import com.hsr.springai.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Update_02_BuiltinProviders implements UpdateRunner {

    private final ProviderRepository providerRepository;

    @Override
    @Transactional
    public void run() {
        List<Provider> builtins = providerRepository.findByIsBuiltinTrue();
        if (!builtins.isEmpty()) {
            log.info("内置提供商已存在，跳过初始化");
            return;
        }

        // OpenAI
        Provider openai = new Provider();
        openai.setName("OpenAI");
        openai.setType("openai");
        openai.setApiKey("");
        openai.setBaseUrl("https://api.openai.com");
        openai.setStatus("enabled");
        openai.setIconColor("#10B981");
        openai.setIsBuiltin(true);
        providerRepository.save(openai);

        // Anthropic
        Provider anthropic = new Provider();
        anthropic.setName("Anthropic");
        anthropic.setType("anthropic");
        anthropic.setApiKey("");
        anthropic.setBaseUrl("https://api.anthropic.com");
        anthropic.setStatus("enabled");
        anthropic.setIconColor("#E85D75");
        anthropic.setIsBuiltin(true);
        providerRepository.save(anthropic);

        // Ollama
        Provider ollama = new Provider();
        ollama.setName("Ollama");
        ollama.setType("ollama");
        ollama.setApiKey("");
        ollama.setBaseUrl("http://localhost:11434");
        ollama.setStatus("enabled");
        ollama.setIconColor("#F97316");
        ollama.setIsBuiltin(true);
        providerRepository.save(ollama);

        log.info("内置提供商初始化完成: OpenAI, Anthropic, Ollama");
    }
}
