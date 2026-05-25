package com.hsr.springai.support.query;

import com.hsr.springai.dto.page.PageQueryRequest;
import com.hsr.springai.dto.page.QueryCondition;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class JpaPageQueryHelper {

    private static final int DEFAULT_PAGE_NO = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 200;

    private JpaPageQueryHelper() {
    }

    public static Pageable pageable(PageQueryRequest request, Map<String, String> allowedFields, String defaultSortBy) {
        int pageNo = request.getPageNo() == null || request.getPageNo() < 1 ? DEFAULT_PAGE_NO : request.getPageNo();
        int pageSize = request.getPageSize() == null || request.getPageSize() < 1 ? DEFAULT_PAGE_SIZE : request.getPageSize();
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);

        String requestedSort = hasText(request.getSortBy()) ? request.getSortBy().trim() : defaultSortBy;
        String sortBy = allowedFields.getOrDefault(requestedSort, defaultSortBy);
        Sort.Direction direction = "asc".equalsIgnoreCase(request.getSortDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(pageNo - 1, pageSize, Sort.by(direction, sortBy));
    }

    public static <T> Specification<T> specification(PageQueryRequest request, Map<String, String> allowedFields) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getConditions() == null) {
                return cb.conjunction();
            }

            for (QueryCondition condition : request.getConditions()) {
                if (condition == null || isEmptyValue(condition.getValue())) {
                    continue;
                }

                String operator = hasText(condition.getOperator())
                        ? condition.getOperator().trim().toLowerCase(Locale.ROOT)
                        : "eq";

                if ("likeany".equals(operator)) {
                    List<Predicate> likes = buildLikeAnyPredicates(condition, allowedFields, root, cb);
                    if (!likes.isEmpty()) {
                        predicates.add(cb.or(likes.toArray(new Predicate[0])));
                    }
                    continue;
                }

                String field = allowedFields.get(condition.getField());
                if (field == null) {
                    continue;
                }

                if ("like".equals(operator)) {
                    predicates.add(cb.like(cb.lower(root.get(field).as(String.class)), "%" + valueAsString(condition.getValue()).toLowerCase(Locale.ROOT) + "%"));
                } else {
                    predicates.add(cb.equal(root.get(field), condition.getValue()));
                }
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static <T> Specification<T> equal(String field, Object value) {
        return (root, query, cb) -> cb.equal(root.get(field), value);
    }

    private static <T> List<Predicate> buildLikeAnyPredicates(
            QueryCondition condition,
            Map<String, String> allowedFields,
            jakarta.persistence.criteria.Root<T> root,
            jakarta.persistence.criteria.CriteriaBuilder cb
    ) {
        List<Predicate> predicates = new ArrayList<>();
        if (condition.getFields() == null) {
            return predicates;
        }

        String value = "%" + valueAsString(condition.getValue()).toLowerCase(Locale.ROOT) + "%";
        for (String requestedField : condition.getFields()) {
            String field = allowedFields.get(requestedField);
            if (field != null) {
                predicates.add(cb.like(cb.lower(root.get(field).as(String.class)), value));
            }
        }
        return predicates;
    }

    private static String valueAsString(Object value) {
        return String.valueOf(value).trim();
    }

    private static boolean isEmptyValue(Object value) {
        return value == null || (value instanceof String str && str.trim().isEmpty());
    }

    private static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
