package com.hsr.springai.dto.page;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageQueryRequest {

    private Integer pageNo = 1;

    private Integer pageSize = 10;

    private String sortBy;

    private String sortDirection = "desc";

    private List<QueryCondition> conditions = new ArrayList<>();
}
