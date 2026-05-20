package com.hsr.springai.updater;

import com.hsr.springai.entity.Assistant;
import com.hsr.springai.entity.Provider;
import com.hsr.springai.entity.Topic;
import com.hsr.springai.entity.User;
import com.hsr.springai.repository.AssistantRepository;
import com.hsr.springai.repository.ProviderRepository;
import com.hsr.springai.repository.TopicRepository;
import com.hsr.springai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class Update_03_BuiltinAssistaAndTopic implements UpdateRunner {

    private final AssistantRepository assistantRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run() {
        List<Assistant> builtins = assistantRepository.findByIsBuiltin(true);
        if (!builtins.isEmpty()) {
            log.info("内置助手已存在，跳过初始化");
            return;
        }

        Optional<User> admin = userRepository.findByUsername("admin");

        // 初始化助手
        Assistant assistant = new Assistant();
        assistant.setDescription("内置助手，用于提供默认的助手服务");
        assistant.setName("默认助手");
        assistant.setIsBuiltin(true);
        assistant.setUserId(admin.get().getId());
        assistantRepository.save(assistant);

        // 初始化话题
        Topic topic = new Topic();
        topic.setName("默认话题");
        topic.setDescription("内置话题，用于提供默认的话题服务");
        topic.setIsBuiltin(true);
        topic.setAssistant(assistant);
        topic.setAssistant(assistant);

        log.info("内置提供商初始化完成: OpenAI, Anthropic, Ollama");
    }
}
