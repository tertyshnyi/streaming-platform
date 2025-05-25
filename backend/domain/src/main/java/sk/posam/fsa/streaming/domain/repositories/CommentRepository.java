package sk.posam.fsa.streaming.domain.repositories;

import sk.posam.fsa.streaming.domain.models.entities.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> get(Long id);
    Comment create(Comment comment);
    Comment update(Comment comment);
    void delete(Long id);
    List<Comment> findByMediaContentId(Long mediaContentId);
    List<Comment> findByParentCommentId(Long parentCommentId);
}
