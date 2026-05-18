package com.hsr.springai.service;

import com.hsr.springai.entity.Assistant;
import com.hsr.springai.entity.Topic;
import com.hsr.springai.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final AssistantService assistantService;

    public List<Topic> findByAssistantId(Long assistantId) {
        return topicRepository.findByAssistantId(assistantId);
    }

    @Transactional
    public Topic create(Long assistantId, String name, String description) {
        Assistant assistant = assistantService.findById(assistantId);
        Topic topic = new Topic();
        topic.setName(name);
        topic.setDescription(description);
        topic.setAssistant(assistant);
        topic.setModelId(assistant.getModelId());
        return topicRepository.save(topic);
    }

    @Transactional
    public Topic updateModelId(Long id, Long modelId) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("话题不存在: " + id));
        topic.setModelId(modelId);
        return topicRepository.save(topic);
    }

    @Transactional
    public Topic updateName(Long id, String name) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("话题不存在: " + id));
        topic.setName(name);
        return topicRepository.save(topic);
    }

    @Transactional
    public void delete(Long id) {
        topicRepository.deleteById(id);
    }
}
