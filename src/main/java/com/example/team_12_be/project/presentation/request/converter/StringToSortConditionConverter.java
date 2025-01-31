package com.example.team_12_be.project.presentation.request.converter;

import com.example.team_12_be.project.presentation.request.SortCondition;

public class StringToSortConditionConverter implements org.springframework.core.convert.converter.Converter<String, SortCondition> {

    @Override
    public SortCondition convert(String sortCondition) {
        return SortCondition.valueOf(sortCondition.toUpperCase());
    }
}