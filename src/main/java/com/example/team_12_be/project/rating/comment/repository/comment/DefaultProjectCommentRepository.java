package com.example.team_12_be.project.rating.comment.repository.comment;

import com.example.team_12_be.project.rating.comment.domain.ProjectCommentRepository;
import com.example.team_12_be.project.rating.comment.domain.RatingReply;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DefaultProjectCommentRepository implements ProjectCommentRepository {

    private final ProjectCommentJpaRepository projectCommentJpaRepository;

    @Override
    public RatingReply save(RatingReply ratingReply) {
        return projectCommentJpaRepository.save(ratingReply);
    }

    @Override
    public Optional<RatingReply> findById(Long id) {
        return projectCommentJpaRepository.findById(id);
    }


    @Override
    public void deleteAllByProjectRatingId(Long ratingId) {
        projectCommentJpaRepository.deleteAllByProjectRatingId(ratingId);
    }

    @Override
    public Page<RatingReply> findAllByProjectIdWithMember(Long ratingId, Pageable pageable) {
        return projectCommentJpaRepository.findAllByProjectRatingIdWithMember(ratingId, pageable);
    }

    @Override
    public long countByProjectRatingId(Long ratingId) {
        return projectCommentJpaRepository.countByProjectRatingId(ratingId);
    }

    @Override
    public Page<RatingReply> findAllByParentCommentId(Long parentCommentId, Pageable pageable) {
        return projectCommentJpaRepository.findAllByParentIdWithMember(parentCommentId, pageable);
    }

    @Override
    public Optional<RatingReply> findByIdAndMemberId(Long id, Long memberId) {
        return projectCommentJpaRepository.findByIdAndMemberId(id, memberId);
    }

    @Override
    public List<RatingReply> findAllByParentId(Long parentId) {
        return projectCommentJpaRepository.findAllByParentId(parentId);
    }

    @Override
    public void delete(RatingReply ratingReply) {
        projectCommentJpaRepository.delete(ratingReply);
    }

    @Override
    public void deleteAll(List<RatingReply> ratingReplies) {
        projectCommentJpaRepository.deleteAll(ratingReplies);
    }
}