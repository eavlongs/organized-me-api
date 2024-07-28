package com.organized_me.api.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.Optional;

public class Helper {
    public static <T> void setResponsePaginatedData(Map<String, Object> data, Page<T> pageObject, String dataKey) {
        data.put(dataKey, pageObject.getContent());
        data.put("total", pageObject.getTotalElements());
        data.put("page", pageObject.getNumber() + 1);
        data.put("limit", pageObject.getSize());
    }

    public static Pageable setUpPageable(int page, int limit, String sortKey, String sortType) {
        if (sortType.equals("asc")) {
            return PageRequest.of(page - 1, limit, Sort.by(sortKey).ascending());
        } else if (sortType.equals("desc")) {
            return PageRequest.of(page - 1, limit, Sort.by(sortKey).descending());
        }
        return PageRequest.of(page - 1, limit);
    }
    
    public static String getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                .orElse("");
    }
}
