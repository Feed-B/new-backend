package com.example.team_12_be.global.presentation;

public record CustomPageRequest(
        int page,
        int size
) {

    private static final int MAXIMUM_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 4;

    @Override
    public int page() {
        if (page < 1) {
            return 0;
        }

        return page - 1;
    }

    @Override
    public int size() {
        if (size > MAXIMUM_SIZE) {
            return DEFAULT_PAGE_SIZE;
        }

        return Math.max(size, DEFAULT_PAGE_SIZE);
    }
}