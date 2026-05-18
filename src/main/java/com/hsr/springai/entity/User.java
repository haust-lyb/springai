package com.hsr.springai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_account")
public class User extends BaseEntity {

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(length = 50)
    private String nickname;

    @Column(length = 100)
    private String email;

    /**
     * 用户角色：user-普通用户，admin-管理员
     */
    @Column(length = 20)
    private String role = "user";

    /**
     * 用户状态：enabled-启用，disabled-禁用
     */
    @Column(length = 20)
    private String status = "enabled";
}
