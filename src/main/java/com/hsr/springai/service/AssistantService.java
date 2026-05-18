package com.hsr.springai.service;

import com.hsr.springai.entity.Assistant;
import com.hsr.springai.entity.Model;
import com.hsr.springai.entity.SystemSetting;
import com.hsr.springai.entity.Topic;
import com.hsr.springai.repository.AssistantRepository;
import com.hsr.springai.repository.ModelRepository;
import com.hsr.springai.repository.SystemSettingRepository;
import com.hsr.springai.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssistantService {

    private final AssistantRepository assistantRepository;
    private final TopicRepository topicRepository;
    private final SystemSettingRepository systemSettingRepository;
    private final ModelRepository modelRepository;
    private final UserService userService;

    /**
     * 获取当前用户的所有助手
     */
    public List<Assistant> findAll() {
        return assistantRepository.findByUserId(userService.getCurrentUserId());
    }

    /**
     * 检查助手是否属于当前用户
     */
    public Assistant findById(Long id) {
        Assistant assistant = assistantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("助手不存在: " + id));
        // 管理员可以查看所有助手，普通用户只能查看自己的
        if (!userService.isAdmin() && !assistant.getUserId().equals(userService.getCurrentUserId())) {
            throw new RuntimeException("无权访问此助手");
        }
        return assistant;
    }

    @Transactional
    public Assistant create(String name, String description) {
        return create(name, description, null);
    }

    @Transactional
    public Assistant create(String name, String description, Long modelId) {
        Assistant assistant = new Assistant();
        assistant.setUserId(userService.getCurrentUserId());
        assistant.setName(name);
        assistant.setDescription(description);

        if (modelId != null) {
            // 使用指定的模型
            Optional<Model> modelOpt = modelRepository.findById(modelId);
            if (modelOpt.isPresent()) {
                Model model = modelOpt.get();
                assistant.setModelId(model.getId());
                assistant.setModelName(model.getName());
            }
        } else {
            // 读取系统默认模型设置
            Optional<SystemSetting> defaultModelSetting = systemSettingRepository.findByConfigKey("default_model_id");
            if (defaultModelSetting.isPresent() && defaultModelSetting.get().getConfigValue() != null) {
                try {
                    Long defaultModelId = Long.valueOf(defaultModelSetting.get().getConfigValue());
                    Optional<Model> modelOpt = modelRepository.findById(defaultModelId);
                    if (modelOpt.isPresent()) {
                        Model model = modelOpt.get();
                        assistant.setModelId(model.getId());
                        assistant.setModelName(model.getName());
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        assistant = assistantRepository.save(assistant);

        Topic topic = new Topic();
        topic.setUserId(userService.getCurrentUserId());
        topic.setName("默认话题");
        topic.setDescription("刚刚创建的话题");
        topic.setAssistant(assistant);
        topic.setModelId(assistant.getModelId());
        topicRepository.save(topic);

        return assistant;
    }

    @Transactional
    public Assistant update(Long id, String name, String description,
                            Long modelId, Double temperature,
                            Integer maxTokens, Double topP, String systemPrompt) {
        Assistant assistant = findById(id);
        // 权限校验
        if (!userService.isAdmin() && !assistant.getUserId().equals(userService.getCurrentUserId())) {
            throw new RuntimeException("无权修改此助手");
        }
        assistant.setName(name);
        assistant.setDescription(description);
        if (temperature != null) assistant.setTemperature(temperature);
        if (maxTokens != null) assistant.setMaxTokens(maxTokens);
        if (topP != null) assistant.setTopP(topP);
        if (systemPrompt != null) assistant.setSystemPrompt(systemPrompt);

        // 更新模型 ID 和名称
        if (modelId != null) {
            Optional<Model> modelOpt = modelRepository.findById(modelId);
            if (modelOpt.isPresent()) {
                Model model = modelOpt.get();
                assistant.setModelId(model.getId());
                assistant.setModelName(model.getName());
            }
        }

        return assistantRepository.save(assistant);
    }

    @Transactional
    public void delete(Long id) {
        Assistant assistant = findById(id);
        if (!userService.isAdmin() && !assistant.getUserId().equals(userService.getCurrentUserId())) {
            throw new RuntimeException("无权删除此助手");
        }
        assistantRepository.deleteById(id);
    }

    @Transactional
    public Assistant copy(Long id) {
        Assistant source = findById(id);
        if (!userService.isAdmin() && !source.getUserId().equals(userService.getCurrentUserId())) {
            throw new RuntimeException("无权复制此助手");
        }
        Assistant copied = new Assistant();
        copied.setUserId(userService.getCurrentUserId());
        copied.setName(source.getName() + " 副本");
        copied.setDescription(source.getDescription());
        copied.setModelId(source.getModelId());
        copied.setModelName(source.getModelName());
        copied.setTemperature(source.getTemperature());
        copied.setMaxTokens(source.getMaxTokens());
        copied.setTopP(source.getTopP());
        copied.setSystemPrompt(source.getSystemPrompt());
        copied = assistantRepository.save(copied);

        List<Topic> sourceTopics = topicRepository.findByAssistantId(id);
        for (Topic t : sourceTopics) {
            Topic nt = new Topic();
            nt.setUserId(userService.getCurrentUserId());
            nt.setName(t.getName());
            nt.setDescription(t.getDescription());
            nt.setAssistant(copied);
            topicRepository.save(nt);
        }
        return copied;
    }

    @Transactional
    public void clearTopics(Long id) {
        Assistant assistant = findById(id);
        if (!userService.isAdmin() && !assistant.getUserId().equals(userService.getCurrentUserId())) {
            throw new RuntimeException("无权操作此助手");
        }
        List<Topic> topics = topicRepository.findByAssistantId(id);
        topicRepository.deleteAll(topics);
    }
}
