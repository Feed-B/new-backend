package com.example.team_12_be.project.rating.comment.repository.comment;

import com.example.team_12_be.project.rating.comment.domain.RatingReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectCommentJpaRepository extends JpaRepository<RatingReply, Long> {

    @Query("SELECT pc FROM RatingReply pc JOIN FETCH pc.member WHERE pc.projectRating.id = :ratingId AND pc.parentId IS NULL ")
    Page<RatingReply> findAllByProjectRatingIdWithMember(@Param("ratingId") Long ratingId, Pageable pageable);

    @Query("SELECT pc FROM RatingReply pc JOIN FETCH pc.member WHERE pc.parentId = :parentId")
    Page<RatingReply> findAllByParentIdWithMember(@Param("parentId") Long parentId, Pageable pageable);

    Optional<RatingReply> findByProjectRatingIdAndMemberId(Long ratingId, Long authorId);

    long countByProjectRatingId(Long ratingId);

    Optional<RatingReply> findByIdAndMemberId(Long projectId, Long authorId);

    List<RatingReply> findAllByParentId(Long parentId);

    void deleteAllByProjectRatingId(Long ratingId);
}
