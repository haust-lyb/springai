package com.hsr.springai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStats {

    private long total;

    private long enabled;

    private long disabled;

    private long admin;

    private long builtin;
}
