package com.hsr.springai.dto.page;

import lombok.Data;

import java.util.List;

@Data
public class QueryCondition {

    /**
     * 查询字段。多字段模糊查询时可作为业务标识使用。
     */
    private String field;

    /**
     * 支持 eq、like、likeAny。
     */
    private String operator;

    private Object value;

    /**
     * likeAny 使用的字段列表。
     */
    private List<String> fields;
}
