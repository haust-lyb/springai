package com.hsr.springai.repository;

import com.hsr.springai.entity.Assistant;
import com.hsr.springai.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findByAssistantId(Long assistantId);
    List<Topic> findByIsBuiltin(Boolean isBuiltin);


}
