package com.hsr.springai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "system_setting")
public class SystemSetting extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String configKey;

    @Column(length = 2000)
    private String configValue;
}
