package com.example.team_12_be.global.page;


import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
public class CustomPageResponse<T> {

    private final List<T> content;

    private final CustomPageable<T> customPageable;

    public CustomPageResponse(List<T> content, Pageable pageable, long numberOfElements) {
        this.content = content;
        this.customPageable = new CustomPageable<>(new PageImpl<>(content, pageable, numberOfElements));
    }
}