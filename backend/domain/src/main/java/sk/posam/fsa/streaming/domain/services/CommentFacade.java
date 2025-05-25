package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.Comment;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.models.entities.MediaContent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentFacade {
    Comment createComment(Comment comment);
    Comment createReply(User user, Long parentCommentId, String content);
    Optional<Comment> get(Long id);
    Comment update(Comment comment);
    void delete(Long id);
    List<Comment> findByMediaContent(Long mediaContentId);
    List<Comment> getCommentTreeByParentComment(Long parentCommentId);
}

