package com.hsr.springai.controller;

import com.hsr.springai.dto.Result;
import com.hsr.springai.entity.Assistant;
import com.hsr.springai.entity.Topic;
import com.hsr.springai.service.AssistantService;
import com.hsr.springai.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assistants")
@RequiredArgsConstructor
public class AssistantController {

    private final AssistantService assistantService;
    private final TopicService topicService;

    @GetMapping
    public Result<Map<String, Object>> list() {
        List<Assistant> assistants = assistantService.findAll();
        List<Topic> allTopics = assistants.stream()
                .flatMap(a -> a.getTopics().stream())
                .collect(Collectors.toList());

        List<Map<String, Object>> assistantList = assistants.stream().map(a -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", a.getId());
            map.put("name", a.getName());
            map.put("desc", a.getDescription());
            map.put("modelId", a.getModelId());
            map.put("modelName", a.getModelName());
            map.put("temperature", a.getTemperature());
            map.put("maxTokens", a.getMaxTokens());
            map.put("topP", a.getTopP());
            map.put("systemPrompt", a.getSystemPrompt());
            return map;
        }).collect(Collectors.toList());

        List<Map<String, Object>> topicList = allTopics.stream().map(t -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", t.getId());
            map.put("name", t.getName());
            map.put("desc", t.getDescription());
            map.put("assistantId", t.getAssistant().getId());
            map.put("modelId", t.getModelId());
            return map;
        }).collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("assistants", assistantList);
        data.put("topics", topicList);
        return Result.success(data);
    }

    @PostMapping
    public Result<Map<String, Object>> create(@RequestBody Map<String, Object> body) {
        String name = body.getOrDefault("name", "默认助手").toString();
        String desc = body.getOrDefault("desc", "").toString();
        Long modelId = body.get("modelId") != null ? Long.valueOf(body.get("modelId").toString()) : null;
        Assistant assistant = assistantService.create(name, desc, modelId);

        Map<String, Object> map = new HashMap<>();
        map.put("id", assistant.getId());
        map.put("name", assistant.getName());
        map.put("desc", assistant.getDescription());
        map.put("modelId", assistant.getModelId());
        map.put("modelName", assistant.getModelName());
        return Result.success(map);
    }

    @PostMapping("/{id}/update")
    public Result<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String desc = (String) body.get("desc");
        Long modelId = body.get("modelId") != null ? Long.valueOf(body.get("modelId").toString()) : null;
        Double temperature = body.get("temperature") != null ? Double.valueOf(body.get("temperature").toString()) : null;
        Integer maxTokens = body.get("maxTokens") != null ? Integer.valueOf(body.get("maxTokens").toString()) : null;
        Double topP = body.get("topP") != null ? Double.valueOf(body.get("topP").toString()) : null;
        String systemPrompt = (String) body.get("systemPrompt");

        Assistant assistant = assistantService.update(id, name, desc, modelId, temperature, maxTokens, topP, systemPrompt);
        Map<String, Object> map = new HashMap<>();
        map.put("id", assistant.getId());
        map.put("name", assistant.getName());
        map.put("desc", assistant.getDescription());
        map.put("modelId", assistant.getModelId());
        map.put("modelName", assistant.getModelName());
        return Result.success(map);
    }

    @PostMapping("/{id}/delete")
    public Result<Void> delete(@PathVariable Long id) {
        assistantService.delete(id);
        return Result.success();
    }

    @PostMapping("/{id}/copy")
    public Result<Map<String, Object>> copy(@PathVariable Long id) {
        Assistant assistant = assistantService.copy(id);
        Map<String, Object> map = new HashMap<>();
        map.put("id", assistant.getId());
        map.put("name", assistant.getName());
        map.put("desc", assistant.getDescription());
        map.put("modelId", assistant.getModelId());
        map.put("modelName", assistant.getModelName());
        return Result.success(map);
    }

    @PostMapping("/{id}/topics/clear")
    public Result<Void> clearTopics(@PathVariable Long id) {
        assistantService.clearTopics(id);
        return Result.success();
    }
}
