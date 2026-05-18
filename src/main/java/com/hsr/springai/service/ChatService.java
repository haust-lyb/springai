package com.hsr.springai.service;

import cn.hutool.core.util.StrUtil;
import com.hsr.springai.entity.*;
import com.hsr.springai.repository.ModelRepository;
import com.hsr.springai.repository.ProviderRepository;
import com.hsr.springai.repository.TopicRepository;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.anthropic.api.AnthropicApi;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final TopicRepository topicRepository;
    private final UserService userService;
    private final ModelRepository modelRepository;
    private final ProviderRepository providerRepository;

    /**
     * 发送流式对话
     */
    @Transactional
    public SseEmitter streamChat(Long topicId, String userContent, Long modelId) {
        Long userId = userService.getCurrentUserId();

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("话题不存在"));

        if (!topic.getUserId().equals(userId)) {
            throw new RuntimeException("无权访问此话题");
        }

        Assistant assistant = topic.getAssistant();

        Model model = null;
        Provider provider = null;

        // 确定使用的模型
        if (modelId != null) {
            model = modelRepository.findById(modelId).orElse(null);
        }
        if (model == null && topic.getModelId() != null) {
            model = modelRepository.findById(topic.getModelId()).orElse(null);
        }
        if (model == null && assistant != null && assistant.getModelId() != null) {
            model = modelRepository.findById(assistant.getModelId()).orElse(null);
        }

        if (model != null) {
            provider = providerRepository.findById(model.getProvider().getId()).orElse(null);
        }

        // 创建SSE emitter 超时时间 = 5分钟
        SseEmitter emitter = new SseEmitter(Duration.ofMinutes(5).toMillis());

        // 异步处理流式回复
        processStreamResponse(emitter, topicId, userId, userContent, model, provider);

        return emitter;
    }

    private void processStreamResponse(SseEmitter emitter, Long topicId, Long userId,
                                        String userContent,
                                       Model model, Provider provider) {

    }

    private void streamWithOpenAi(SseEmitter emitter, List<Message> messages, Model model, Provider provider,
                                   StringBuilder fullResponse, CountDownLatch latch) {
        String apiKey = provider.getApiKey() != null ? provider.getApiKey() : "none";
        String baseUrl = provider.getBaseUrl();

        var apiBuilder = OpenAiApi.builder().apiKey(apiKey);
        if (baseUrl != null && !baseUrl.isBlank()) {
            apiBuilder.baseUrl(baseUrl);
        }
        OpenAiApi openAiApi = apiBuilder.build();

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(model.getName())
                .temperature(0.7)
                .build();

        OpenAiChatModel chatModel = new OpenAiChatModel(
                openAiApi,
                options,
                ToolCallingManager.builder().build(),
                RetryTemplate.defaultInstance(),
                ObservationRegistry.create()
        );

        chatModel.stream(new Prompt(messages)).subscribe(
                chatResponse -> {
                    String content = chatResponse.getResult() != null && chatResponse.getResult().getOutput() != null
                            ? chatResponse.getResult().getOutput().getText() : null;
                    if (content != null) {
                        try {
                            fullResponse.append(content);
                            emitter.send(SseEmitter.event().data(content).name("content"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                error -> {
                    try {
                        emitter.send(SseEmitter.event().data(error.getMessage()).name("error"));
                    } catch (IOException e) {
                        // ignore
                    }
                    latch.countDown();
                },
                () -> latch.countDown()
        );
    }

    private void streamWithOllama(SseEmitter emitter, List<Message> messages, Model model, Provider provider,
                                   StringBuilder fullResponse, CountDownLatch latch) {
        String baseUrl = provider.getBaseUrl();

        var apiBuilder = OllamaApi.builder();
        if (baseUrl != null && !baseUrl.isBlank()) {
            apiBuilder.baseUrl(baseUrl);
        }
        OllamaApi ollamaApi = apiBuilder.build();

        OllamaChatOptions options = OllamaChatOptions.builder()
                .model(model.getName())
                .temperature(0.7)
                .build();

        OllamaChatModel chatModel = new OllamaChatModel(
                ollamaApi,
                options,
                ToolCallingManager.builder().build(),
                ObservationRegistry.create(),
                ModelManagementOptions.builder().build()
        );

        chatModel.stream(new Prompt(messages)).subscribe(
                chatResponse -> {
                    String content = chatResponse.getResult() != null && chatResponse.getResult().getOutput() != null
                            ? chatResponse.getResult().getOutput().getText() : null;
                    if (content != null) {
                        try {
                            fullResponse.append(content);
                            emitter.send(SseEmitter.event().data(content).name("content"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                error -> {
                    try {
                        emitter.send(SseEmitter.event().data(error.getMessage()).name("error"));
                    } catch (IOException e) {
                        // ignore
                    }
                    latch.countDown();
                },
                () -> latch.countDown()
        );
    }

    private void streamWithAnthropic(SseEmitter emitter, List<Message> messages, Model model, Provider provider,
                                      StringBuilder fullResponse, CountDownLatch latch) {
        String apiKey = provider.getApiKey() != null ? provider.getApiKey() : "none";
        String baseUrl = provider.getBaseUrl();

        var apiBuilder = AnthropicApi.builder().apiKey(apiKey);
        if (baseUrl != null && !baseUrl.isBlank()) {
            apiBuilder.baseUrl(baseUrl);
        }
        AnthropicApi anthropicApi = apiBuilder.build();

        AnthropicChatOptions options = AnthropicChatOptions.builder()
                .model(model.getName())
                .temperature(0.7)
                .build();

        AnthropicChatModel chatModel = new AnthropicChatModel(
                anthropicApi,
                options,
                ToolCallingManager.builder().build(),
                RetryTemplate.defaultInstance(),
                ObservationRegistry.create()
        );

        chatModel.stream(new Prompt(messages)).subscribe(
                chatResponse -> {
                    String content = chatResponse.getResult() != null && chatResponse.getResult().getOutput() != null
                            ? chatResponse.getResult().getOutput().getText() : null;
                    if (content != null) {
                        try {
                            fullResponse.append(content);
                            emitter.send(SseEmitter.event().data(content).name("content"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                error -> {
                    try {
                        emitter.send(SseEmitter.event().data(error.getMessage()).name("error"));
                    } catch (IOException e) {
                        // ignore
                    }
                    latch.countDown();
                },
                () -> latch.countDown()
        );
    }

    private void streamMockResponse(SseEmitter emitter, String userContent,
                                    Model model, Provider provider,
                                    StringBuilder fullResponse) throws IOException, InterruptedException {
        String modelInfo = model != null ? "使用模型：" + model.getName() + " (" + (provider != null ? provider.getName() : "unknown") + ")" : "未配置模型";
        String base = "你好！\n\n" + modelInfo + "\n\n" +
                "我收到了你的问题：\"" + userContent + "\"\n\n" +
                "请确保提供商配置中的API Key和Base URL正确。";

        for (char c : base.toCharArray()) {
            fullResponse.append(c);
            emitter.send(SseEmitter.event().data(String.valueOf(c)).name("content"));
            Thread.sleep(25);
        }
    }
}
