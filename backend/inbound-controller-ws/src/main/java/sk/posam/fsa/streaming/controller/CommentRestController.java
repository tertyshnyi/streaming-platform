package sk.posam.fsa.streaming.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.streaming.domain.models.entities.*;
import sk.posam.fsa.streaming.domain.services.*;
import sk.posam.fsa.streaming.mapper.CommentMapper;
import sk.posam.fsa.streaming.rest.api.CommentsApi;
import sk.posam.fsa.streaming.rest.dto.CommentDto;
import sk.posam.fsa.streaming.rest.dto.CreateCommentDto;
import sk.posam.fsa.streaming.rest.dto.UserDto;
import sk.posam.fsa.streaming.security.CurrentUserDetailService;

import java.util.List;
import java.util.Optional;

@RestController
public class CommentRestController implements CommentsApi {

    private final UserFacade userFacade;
    private final CurrentUserDetailService currentUserDetailService;
    private final CommentFacade commentFacade;
    private final CommentMapper commentMapper;
    private final UnifiedMediaContentFacade mediaContentFacade;

    public CommentRestController(UserFacade userFacade,
                                 CurrentUserDetailService currentUserDetailService,
                                 CommentFacade commentFacade,
                                 CommentMapper commentMapper,
                                 UnifiedMediaContentFacade mediaContentFacade) {
        this.userFacade = userFacade;
        this.currentUserDetailService = currentUserDetailService;
        this.commentFacade = commentFacade;
        this.commentMapper = commentMapper;
        this.mediaContentFacade = mediaContentFacade;
    }

    @Override
    public ResponseEntity<CommentDto> createComment(CreateCommentDto dto) {
        UserDto currentUser = currentUserDetailService.getCurrentUser();

        User user = userFacade.findByKeycloakId(currentUser.getKeycloakId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        MediaContent mediaContent = findMediaContentById(dto.getMediaContentId())
                .orElseThrow(() -> new RuntimeException("Media content not found"));

        Comment comment;

        if (dto.getParentCommentId() != null) {
            Comment parentComment = commentFacade.get(dto.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));

            if (!parentComment.getMediaContent().getId().equals(dto.getMediaContentId())) {
                throw new RuntimeException("Parent comment does not belong to the specified media content");
            }

            comment = commentFacade.createReply(user, dto.getParentCommentId(), dto.getText());

            commentFacade.incrementChildrenCount(dto.getParentCommentId());
        } else {
            comment = commentMapper.toEntity(dto, user, mediaContent, null);
            comment = commentFacade.createComment(comment);
        }

        return ResponseEntity.ok(commentMapper.toDto(comment));
    }

    @Override
    public ResponseEntity<CommentDto> updateComment(Long id, CreateCommentDto dto) {
        Comment comment = commentFacade.get(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setContent(dto.getText());
        Comment updated = commentFacade.update(comment);
        return ResponseEntity.ok(commentMapper.toDto(updated));
    }

    @Override
    public ResponseEntity<CommentDto> getCommentById(Long id) {
        CommentDto dto = commentFacade.get(id)
                .map(commentMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long id) {
        Comment comment = commentFacade.get(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (comment.getParentComment() != null) {
            commentFacade.decrementChildrenCount(comment.getParentComment().getId());
        }

        commentFacade.delete(id);
        mediaContentFacade.decrementCommentsTotal(comment.getMediaContent().getId());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<CommentDto>> getCommentsByMediaContentId(Long mediaContentId) {
        List<CommentDto> dtos = commentFacade.findByMediaContent(mediaContentId)
                .stream()
                .map(commentMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<List<CommentDto>> getCommentTreeByParentCommentId(Long parentCommentId) {
        List<Comment> commentTree = commentFacade.getCommentTreeByParentComment(parentCommentId);

        List<CommentDto> dtos = commentTree.stream()
                .map(commentMapper::toDtoWithChildren)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    private Optional<MediaContent> findMediaContentById(Long id) {
        return mediaContentFacade.get(id);
    }
}
