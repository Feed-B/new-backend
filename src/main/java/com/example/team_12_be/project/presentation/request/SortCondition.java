package com.example.team_12_be.project.presentation.request;

import com.example.team_12_be.project.domain.QProject;
import com.querydsl.core.types.OrderSpecifier;
import lombok.Getter;

@Getter
public enum SortCondition {

    LIKES() {
        @Override
        public OrderSpecifier<?>[] getSpecifier(QProject project) {
            return new OrderSpecifier<?>[]{project.projectLikes.size().desc(), project.createdAt.desc()};
        }
    },
    VIEWS() {
        @Override
        public OrderSpecifier<?>[] getSpecifier(QProject project) {
            return new OrderSpecifier<?>[]{project.viewCount.desc(), project.createdAt.desc()};
        }
    },
    RECENT() {
        @Override
        public OrderSpecifier<?>[] getSpecifier(QProject project) {
            return new OrderSpecifier<?>[]{project.createdAt.desc()};
        }
    };

    public abstract OrderSpecifier<?>[] getSpecifier(QProject project);
}