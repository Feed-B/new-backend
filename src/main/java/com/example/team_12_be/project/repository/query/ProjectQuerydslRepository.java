package com.example.team_12_be.project.repository.query;

import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.presentation.request.SortCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.example.team_12_be.project.domain.QProject.project;
import static com.example.team_12_be.project.domain.QProjectLike.projectLike;

@RequiredArgsConstructor
@Repository
public class ProjectQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private BooleanExpression filterByProjectTechStacks(List<String> projectTechStacks) {
        if (Objects.isNull(projectTechStacks) || projectTechStacks.isEmpty()) {
            return null;
        }

        BooleanExpression result = Expressions.asBoolean(true).isTrue();

        for (String techStack : projectTechStacks) {
            result = result.and(project.projectTechStacks.any().techStack.eq(techStack));
        }
        return result;
    }

    private BooleanExpression filterByTitleOrContent(String serachString) {
        if (Objects.isNull(serachString) || serachString.isEmpty()) {
            return null;
        }

        return project.title.like("%" + serachString + "%").or(project.content.like("%" + serachString + "%"));
    }

    public Page<Project> findProjectsProjectTechStacksOrderBySortCondition(SortCondition sortCondition, List<String> projectTechStacks, String searchString, Pageable pageable) {
        List<Project> projectList = jpaQueryFactory.select(project)
                .from(project)
                .where(filterByProjectTechStacks(projectTechStacks), filterByTitleOrContent(searchString))
                .orderBy(sortCondition.getSpecifier(project))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory.select(Expressions.ONE)
                .from(project)
                .leftJoin(project.projectLikes, projectLike)
                .where(filterByProjectTechStacks(projectTechStacks))
                .stream().count();

        return new PageImpl<>(projectList, pageable, total);
    }
}
