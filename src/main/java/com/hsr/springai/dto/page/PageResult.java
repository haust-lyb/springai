package com.hsr.springai.dto.page;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResult<T> {

    private List<T> records;

    private long total;

    private int pageNo;

    private int pageSize;

    private int totalPages;

    private boolean hasNext;

    private boolean hasPrevious;

    public static <T> PageResult<T> from(Page<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(page.getContent());
        result.setTotal(page.getTotalElements());
        result.setPageNo(page.getNumber() + 1);
        result.setPageSize(page.getSize());
        result.setTotalPages(page.getTotalPages());
        result.setHasNext(page.hasNext());
        result.setHasPrevious(page.hasPrevious());
        return result;
    }
}
