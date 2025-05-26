package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.Comment;
import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.repositories.CommentRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommentService implements CommentFacade {

    private final CommentRepository commentRepository;
    private final UnifiedMediaContentFacade mediaContentFacade;

    public CommentService(CommentRepository commentRepository, UnifiedMediaContentFacade mediaContentFacade) {
        this.commentRepository = commentRepository;
        this.mediaContentFacade = mediaContentFacade;
    }

    private void updateCommentsCount(Long mediaContentId) {
        mediaContentFacade.incrementCommentsTotal(mediaContentId);
    }

    @Override
    public Comment createComment(Comment comment) {
        if (comment.getUser() == null) throw new IllegalArgumentException("User must be authenticated");
        if (comment.getMediaContent() == null) throw new IllegalArgumentException("MediaContent must not be null");
        if (comment.getContent() == null || comment.getContent().isBlank()) throw new IllegalArgumentException("Content must not be empty");

        comment.setCreatedAt(LocalDateTime.now());
        comment.setChildrenComments(Collections.emptyList());

        Comment saved = commentRepository.create(comment);

        updateCommentsCount(comment.getMediaContent().getId());

        return saved;
    }

    @Override
    public Comment createReply(User user, Long parentCommentId, String text) {
        Comment parent = commentRepository.get(parentCommentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));

        MediaContent mediaContent = parent.getMediaContent();
        if (mediaContent == null) {
            throw new IllegalStateException("Parent comment has no media content");
        }

        Comment reply = new Comment();
        reply.setMediaContent(mediaContent);
        reply.setParentComment(parent);
        reply.setUser(user);
        reply.setContent(text);
        reply.setCreatedAt(LocalDateTime.now());

        commentRepository.create(reply);
        updateCommentsCount(mediaContent.getId());

        return reply;
    }

    @Override
    public List<Comment> getCommentTreeByParentComment(Long parentCommentId) {
        List<Comment> allComments = commentRepository.findByMediaContentId(
                commentRepository.get(parentCommentId)
                        .orElseThrow(() -> new RuntimeException("Parent comment not found"))
                        .getMediaContent().getId()
        );

        Map<Long, Comment> commentMap = allComments.stream()
                .collect(Collectors.toMap(Comment::getId, Function.identity()));

        Comment root = commentMap.get(parentCommentId);
        if (root == null) {
            throw new RuntimeException("Parent comment not found in media content comments");
        }

        buildChildrenTree(root, commentMap);

        return List.of(root);
    }

    @Override
    public void incrementChildrenCount(Long parentCommentId) {
        commentRepository.get(parentCommentId).ifPresent(parent -> {
            parent.setChildrenCount(parent.getChildrenCount() + 1);
            commentRepository.create(parent);

            if (parent.getParentComment() != null) {
                incrementChildrenCount(parent.getParentComment().getId());
            }
        });
    }

    @Override
    public void decrementChildrenCount(Long parentCommentId) {
        commentRepository.get(parentCommentId).ifPresent(parent -> {
            parent.setChildrenCount(Math.max(0, parent.getChildrenCount() - 1));
            commentRepository.create(parent);

            if (parent.getParentComment() != null) {
                decrementChildrenCount(parent.getParentComment().getId());
            }
        });
    }

    private void buildChildrenTree(Comment parent, Map<Long, Comment> commentMap) {
        List<Comment> children = commentMap.values().stream()
                .filter(c -> c.getParentComment() != null && c.getParentComment().getId().equals(parent.getId()))
                .toList();

        parent.setChildrenComments(children);

        for (Comment child : children) {
            buildChildrenTree(child, commentMap);
        }
    }

    @Override
    public Optional<Comment> get(Long id) {
        return commentRepository.get(id);
    }

    @Override
    public Comment update(Comment comment) {
        return commentRepository.update(comment);
    }

    @Override
    public void delete(Long id) {
        if (commentRepository.get(id).isEmpty()) {
            throw new IllegalArgumentException("Comment not found");
        }
        commentRepository.delete(id);
    }

    @Override
    public List<Comment> findByMediaContent(Long mediaContentId) {
        return commentRepository.findByMediaContentId(mediaContentId);
    }
}
