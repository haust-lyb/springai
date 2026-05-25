package com.hsr.springai.controller;

import com.hsr.springai.dto.Result;
import com.hsr.springai.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/api/chat/topics/history")
    public Result<List<Map<String, Object>>> getChatHistory(@RequestParam Long topicId) {
        log.info("Getting chat history for topic {}", topicId);
        return Result.success(List.of());
    }


    @PostMapping(value = "/api/chat/topics/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(
            @RequestParam Long topicId,
            @RequestBody Map<String, Object> body
    ) {
        String content = (String) body.get("content");
        Long modelId = body.get("modelId") != null ? Long.parseLong(body.get("modelId").toString()) : null;

        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("消息内容不能为空");
        }

        return chatService.streamChat(topicId, content.trim(), modelId);
    }
}
