package com.hsr.springai.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "assistant")
public class Assistant extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "model_id")
    private Long modelId;

    private Double temperature;

    @Column(name = "max_tokens")
    private Integer maxTokens;

    @Column(name = "top_p")
    private Double topP;

    @Column(name = "system_prompt", length = 4000)
    private String systemPrompt;

    @OneToMany(mappedBy = "assistant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createTime ASC")
    private List<Topic> topics = new ArrayList<>();
}
