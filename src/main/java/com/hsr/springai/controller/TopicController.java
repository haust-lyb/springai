package com.hsr.springai.controller;

import com.hsr.springai.dto.Result;
import com.hsr.springai.entity.Topic;
import com.hsr.springai.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @PostMapping
    public Result<Map<String, Object>> create(@RequestBody Map<String, Object> body) {
        Long assistantId = Long.valueOf(body.get("assistantId").toString());
        String name = (String) body.get("name");
        String desc = (String) body.get("desc");
        Topic topic = topicService.create(assistantId, name, desc);

        Map<String, Object> map = new HashMap<>();
        map.put("id", topic.getId());
        map.put("name", topic.getName());
        map.put("desc", topic.getDescription());
        map.put("assistantId", topic.getAssistant().getId());
        map.put("modelId", topic.getModelId());
        return Result.success(map);
    }

    @PostMapping("/{id}/update")
    public Result<Map<String, Object>> updateName(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String name = body.get("name");
        Topic topic = topicService.updateName(id, name);
        Map<String, Object> map = new HashMap<>();
        map.put("id", topic.getId());
        map.put("name", topic.getName());
        map.put("desc", topic.getDescription());
        map.put("assistantId", topic.getAssistant().getId());
        map.put("modelId", topic.getModelId());
        return Result.success(map);
    }

    @PostMapping("/{id}/model")
    public Result<Map<String, Object>> updateModel(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long modelId = body.get("modelId") != null ? Long.valueOf(body.get("modelId").toString()) : null;
        Topic topic = topicService.updateModelId(id, modelId);
        Map<String, Object> map = new HashMap<>();
        map.put("id", topic.getId());
        map.put("name", topic.getName());
        map.put("desc", topic.getDescription());
        map.put("assistantId", topic.getAssistant().getId());
        map.put("modelId", topic.getModelId());
        return Result.success(map);
    }

    @PostMapping("/{id}/delete")
    public Result<Void> delete(@PathVariable Long id) {
        topicService.delete(id);
        return Result.success();
    }
}
