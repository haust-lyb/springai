package com.hsr.springai.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "provider")
public class Provider extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "base_url")
    private String baseUrl;

    @Column(nullable = false)
    private String status = "enabled";

    @Column(name = "icon_color")
    private String iconColor = "#2080f0";

    @Column(length = 50000)
    private String logo;

    @Column(name = "is_builtin")
    private Boolean isBuiltin = false;

    @Column(name = "models_json", length = 4000)
    private String modelsJson;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
