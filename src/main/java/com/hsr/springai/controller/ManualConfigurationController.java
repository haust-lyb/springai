package com.hsr.springai.controller;


import com.hsr.springai.tools.DateTimeTools;
import io.micrometer.observation.ObservationRegistry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.anthropic.api.AnthropicApi;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.SqliteChatMemoryRepositoryDialect;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@Slf4j
@RestController
public class ManualConfigurationController {


    @GetMapping("/api/manual/ai-openai")
    public String getAi(String userInput) {
        log.info("Received user input: {}", userInput);
        var openAiApi = OpenAiApi.builder()
                .apiKey("none")
                .baseUrl("http://10.15.15.16:11434")
//                .baseUrl("http://127.0.0.1:8888")
                .build();
        var openAiChatOptions = OpenAiChatOptions.builder()
                .model("qwen3.5:9b")
                .temperature(0.7)
                .maxTokens(1024 * 2000)
                .extraBody(Map.of("think", false))
                .build();
        var chatModel = new OpenAiChatModel(openAiApi, openAiChatOptions, ToolCallingManager.builder().build(), RetryTemplate.defaultInstance(), ObservationRegistry.create());


        ChatResponse response = chatModel.call(
                new Prompt(userInput));
        String text = response.getResult().getOutput().getText();
        log.info("Received response text: {}", text);
        return text;

//// Or with streaming responses
//        Flux<ChatResponse> response = this.chatModel.stream(
//                new Prompt("Generate the names of 5 famous pirates."));
    }

    @GetMapping("/api/manual/ai-ollama-list-models")
    public String getAiOllamaListModels() {
        var api = OllamaApi.builder()
                .baseUrl("http://10.15.15.16:11434")
//                .baseUrl("http://127.0.0.1:11434")
//                .baseUrl("http://127.0.0.1:8888")
                .build();
        return api.listModels().toString();
    }


    @GetMapping("/api/manual/ai-ollama")
    public String getAiOllama(String userInput) {
        log.info("Received user input: {}", userInput);
        var api = OllamaApi.builder()
                .baseUrl("http://10.15.15.16:11434")
//                .baseUrl("http://127.0.0.1:8888")
                .build();
        var chatOptions = OllamaChatOptions.builder()
                .model("qwen3.5:9b")
                .temperature(0.7)
                .disableThinking()
                .build();
        var chatModel = new OllamaChatModel(api, chatOptions, ToolCallingManager.builder().build(), ObservationRegistry.create(), ModelManagementOptions.builder().build());


        ChatResponse response = chatModel.call(
                new Prompt(userInput));
        String text = response.getResult().getOutput().getText();
        log.info("Received response text: {}", text);
        return text;

//// Or with streaming responses
//        Flux<ChatResponse> response = this.chatModel.stream(
//                new Prompt("Generate the names of 5 famous pirates."));
    }


    @GetMapping("/api/manual/ai-anthropic")
    public String getAiAnthropic(String userInput) {
        log.info("Received user input: {}", userInput);
        var api = AnthropicApi.builder()
                .baseUrl("http://127.0.0.1:8888/anthropic")
//                .baseUrl("https://api.minimaxi.com/anthropic")
//                .apiKey("sk-api-P7gh5cgG524YUqR8V0HIIShhCs-OQlOV12ovlnoCkup0EOVxxnxnuOCwXJX7gGUwhHVfRAY4NT0wszUjsDdPyEu48Wf2tnp69nqNPWeqt-iQPxFjrAGwAo0")
                .apiKey("sk-cp-J16MiTL1ks2hqVqqQVcLzADouRyoVoqn8rPv14VwrKuEXdIbS01BD6-HA2gG0AFtnSIrtlOT49rxGJpgHqi85qlQfwA1qyPSBYu6_k2rO54BOfi-Db4zkwY")
//                .baseUrl("http://127.0.0.1:8888")
                .build();
        var chatOptions = AnthropicChatOptions.builder()
                .model("MiniMax-M2.7")
                .temperature(0.7)
                .build();
        var chatModel = new AnthropicChatModel(api, chatOptions, ToolCallingManager.builder().build(), RetryTemplate.defaultInstance(), ObservationRegistry.create());


        ChatResponse response = chatModel.call(
                new Prompt(userInput));
        String text = response.getResult().getOutput().getText();
        log.info("Received response text: {}", text);
        return text;

//// Or with streaming responses
//        Flux<ChatResponse> response = this.chatModel.stream(
//                new Prompt("Generate the names of 5 famous pirates."));
    }


    @GetMapping("/api/manual/ai-openai-multimodality")
    public String getAiOpenAiMultimodality() {

        var imageResource = new ClassPathResource("/static/multimodal.test.png");

        var userMessage = UserMessage.builder()
                .text("说一下你看到了什么?") // content
                .media(new Media(MimeTypeUtils.IMAGE_PNG, imageResource)) // media
                .build();


        var openAiApi = OpenAiApi.builder()
                .apiKey("none")
//                .baseUrl("http://10.15.15.16:11434")
                .baseUrl("http://127.0.0.1:8888")
                .build();
        var openAiChatOptions = OpenAiChatOptions.builder()
                .model("qwen3.5:9b")
                .temperature(0.7)
                .maxTokens(1024 * 2000)
                .extraBody(Map.of("think", false))
                .build();
        var chatModel = new OpenAiChatModel(openAiApi, openAiChatOptions, ToolCallingManager.builder().build(), RetryTemplate.defaultInstance(), ObservationRegistry.create());


        ChatResponse response = chatModel.call(
                new Prompt(userMessage));
        String text = response.getResult().getOutput().getText();
        log.info("Received response text: {}", text);
        return text;

//// Or with streaming responses
//        Flux<ChatResponse> response = this.chatModel.stream(
//                new Prompt("Generate the names of 5 famous pirates."));
    }


    // Create a memory instance
//    ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

    @Autowired
    JdbcTemplate jdbcTemplate;

    ChatMemory chatMemory;

    @PostConstruct
    public void init() {
        chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(JdbcChatMemoryRepository.builder()
                        .jdbcTemplate(jdbcTemplate)
                        .dialect(new SqliteChatMemoryRepositoryDialect())
                        .build())
                .maxMessages(10)
                .build();
    }

    @GetMapping("/api/manual/ai-openai-memory")
    public String getAiOpenAiMemory(String userInput, String conversationId) {
        log.info("Received user input: {}, conversationId: {}", userInput, conversationId);

        UserMessage userMessage = new UserMessage(userInput);
        chatMemory.add(conversationId, userMessage);

        var openAiApi = OpenAiApi.builder()
                .apiKey("none")
//                .baseUrl("http://10.15.15.16:11434")
                .baseUrl("http://127.0.0.1:8888")
                .build();
        var openAiChatOptions = OpenAiChatOptions.builder()
                .model("qwen3.5:9b")
                .temperature(0.7)
                .maxTokens(1024 * 2000)
                .extraBody(Map.of("think", false))
                .build();
        var chatModel = new OpenAiChatModel(openAiApi, openAiChatOptions, ToolCallingManager.builder().build(), RetryTemplate.defaultInstance(), ObservationRegistry.create());


        ChatResponse response = chatModel.call(
                new Prompt(chatMemory.get(conversationId)));
        chatMemory.add(conversationId, response.getResult().getOutput());
        String text = response.getResult().getOutput().getText();
        log.info("Received response text: {}", text);


        return text;

//// Or with streaming responses
//        Flux<ChatResponse> response = this.chatModel.stream(
//                new Prompt("Generate the names of 5 famous pirates."));
    }


    @GetMapping("/api/manual/ai-openai-memory-tool")
    public String getAiOpenAiMemoryTool(String userInput, String conversationId) {
        log.info("Received user input: {}, conversationId: {}", userInput, conversationId);

        UserMessage userMessage = new UserMessage(userInput);
        chatMemory.add(conversationId, userMessage);

        var openAiApi = OpenAiApi.builder()
                .apiKey("none")
//                .baseUrl("http://10.15.15.16:11434")
                .baseUrl("http://127.0.0.1:8888")
                .build();

        ToolCallback[] dateTimeTools = ToolCallbacks.from(new DateTimeTools());
        var openAiChatOptions = OpenAiChatOptions.builder()
                .model("qwen3.5:9b")
                .temperature(0.7)
                .maxTokens(1024 * 2000)
                .extraBody(Map.of("think", false))
                .toolCallbacks(dateTimeTools)
                .build();

        var chatModel = new OpenAiChatModel(openAiApi, openAiChatOptions, ToolCallingManager.builder().build(), RetryTemplate.defaultInstance(), ObservationRegistry.create());


        ChatResponse response = chatModel.call(
                new Prompt(chatMemory.get(conversationId)));
        chatMemory.add(conversationId, response.getResult().getOutput());
        String text = response.getResult().getOutput().getText();
        log.info("Received response text: {}", text);


        return text;

//// Or with streaming responses
//        Flux<ChatResponse> response = this.chatModel.stream(
//                new Prompt("Generate the names of 5 famous pirates."));
    }


    @GetMapping("/api/manual/ai-openai-sysprompt")
    public String getAiOpenAiSysprompt(String userInput, String conversationId) {
        log.info("Received user input: {}, conversationId: {}", userInput, conversationId);
        if (chatMemory.get(conversationId).isEmpty()) {
            chatMemory.add(conversationId, new SystemMessage("你是猪八戒，用八戒的幽默风趣口吻回答用户问题，另外不要太啰嗦"));
        }

        chatMemory.add(conversationId, new UserMessage(userInput));

        var openAiApi = OpenAiApi.builder()
                .apiKey("none")
//                .baseUrl("http://10.15.15.16:11434")
                .baseUrl("http://127.0.0.1:8888")
                .build();
        // https://docs.ollama.com/api/openai-compatibility#supported-request-fields
        var openAiChatOptions = OpenAiChatOptions.builder()
                .model("qwen3.5:9b")
                .temperature(0.7)
                .extraBody(Map.of("reasoning_effort", "none"))
                .maxTokens(1024 * 2000)
                .build();
        var chatModel = new OpenAiChatModel(openAiApi, openAiChatOptions, ToolCallingManager.builder().build(), RetryTemplate.defaultInstance(), ObservationRegistry.create());

        ChatResponse response = chatModel.call(new Prompt(chatMemory.get(conversationId)));
        chatMemory.add(conversationId, response.getResult().getOutput());
        String text = response.getResult().getOutput().getText();
        log.info("Received response text: {}", text);
        return text;
    }


    @GetMapping(value = "/api/manual/ai-openai-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getAiOpenAiStream(String userInput, String conversationId) {
        log.info("Received user input: {}, conversationId: {}", userInput, conversationId);
        if (chatMemory.get(conversationId).isEmpty()) {
            chatMemory.add(conversationId, new SystemMessage("你是猪八戒，用八戒的幽默风趣口吻回答用户问题，另外不要太啰嗦"));
        }

        chatMemory.add(conversationId, new UserMessage(userInput));

        var openAiApi = OpenAiApi.builder()
                .apiKey("none")
                .baseUrl("http://10.15.15.16:11434")
//                .baseUrl("http://127.0.0.1:8888")
                .build();

        ToolCallback[] dateTimeTools = ToolCallbacks.from(new DateTimeTools());
        // https://docs.ollama.com/api/openai-compatibility#supported-request-fields
        var openAiChatOptions = OpenAiChatOptions.builder()
                .model("qwen3.5:9b")
                .temperature(0.7)
//                .extraBody(Map.of("reasoning_effort", "none"))
                .maxTokens(1024 * 2000)
                .toolCallbacks(dateTimeTools)
                .build();
        var chatModel = new OpenAiChatModel(openAiApi, openAiChatOptions, ToolCallingManager.builder().build(), RetryTemplate.defaultInstance(), ObservationRegistry.create());

        StringBuilder fullResponse = new StringBuilder();

        return chatModel.stream(new Prompt(chatMemory.get(conversationId)))
                .map(resp -> {
                    String content = resp.getResult().getOutput().getText();
                    if (content != null) {
                        fullResponse.append(content);
                        return ServerSentEvent.builder(content).build();
                    }
                    return ServerSentEvent.builder("").build();
                })
                .doOnComplete(() -> {
                    chatMemory.add(conversationId, new AssistantMessage(fullResponse.toString()));
                });
    }


    @GetMapping("/api/manual/ai-openai-memory-tool-jpamemory")
    public String getAiOpenAiMemoryToolJpaMemory(String userInput, String conversationId) {
        log.info("Received user input: {}, conversationId: {}", userInput, conversationId);

        UserMessage userMessage = new UserMessage(userInput);
        chatMemory.add(conversationId, userMessage);

        var openAiApi = OpenAiApi.builder()
                .apiKey("none")
                .baseUrl("http://10.15.15.16:11434")
//                .baseUrl("http://127.0.0.1:8888")
                .build();

        ToolCallback[] dateTimeTools = ToolCallbacks.from(new DateTimeTools());
        var openAiChatOptions = OpenAiChatOptions.builder()
                .model("qwen3.5:9b")
                .temperature(0.7)
                .maxTokens(1024 * 2000)
                .extraBody(Map.of("think", false))
                .toolCallbacks(dateTimeTools)
                .build();

        var chatModel = new OpenAiChatModel(openAiApi, openAiChatOptions, ToolCallingManager.builder().build(), RetryTemplate.defaultInstance(), ObservationRegistry.create());


        ChatResponse response = chatModel.call(
                new Prompt(chatMemory.get(conversationId)));
        chatMemory.add(conversationId, response.getResult().getOutput());
        String text = response.getResult().getOutput().getText();
        log.info("Received response text: {}", text);


        return text;

//// Or with streaming responses
//        Flux<ChatResponse> response = this.chatModel.stream(
//                new Prompt("Generate the names of 5 famous pirates."));
    }
}
