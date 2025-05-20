package sk.posam.fsa.streaming.domain.models.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Comment {
    private Long id;
    private User user;
    private MediaContent mediaContent;
    private String content;
    private LocalDateTime createdAt;

    private Comment parentComment;
    private List<Comment> childrenComments = new ArrayList<>();

    public Comment(Long id, User user, MediaContent mediaContent, String content,
                   LocalDateTime createdAt, Comment parentComment, List<Comment> childrenComments) {
        this.id = id;
        this.user = user;
        this.mediaContent = mediaContent;
        this.content = content;
        this.createdAt = createdAt;
        this.parentComment = parentComment;
        this.childrenComments = childrenComments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MediaContent getMediaContent() {
        return mediaContent;
    }

    public void setMediaContent(MediaContent mediaContent) {
        this.mediaContent = mediaContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public List<Comment> getChildrenComments() {
        return childrenComments;
    }

    public void setChildrenComments(List<Comment> childrenComments) {
        this.childrenComments = childrenComments;
    }

    public void addChildComment(Comment reply) {
        if (reply == null) {
            throw new IllegalArgumentException("reply cannot be null");
        }
        if (reply == this) {
            throw new IllegalArgumentException("Comment cannot be parent himself");
        }
        if (!childrenComments.contains(reply)) {
            childrenComments.add(reply);
            reply.setParentComment(this);
        }
    }
}
