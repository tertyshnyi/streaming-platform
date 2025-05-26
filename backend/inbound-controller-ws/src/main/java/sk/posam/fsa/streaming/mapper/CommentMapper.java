package sk.posam.fsa.streaming.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.Comment;
import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.rest.dto.CommentDto;
import sk.posam.fsa.streaming.rest.dto.CreateCommentDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
public class CommentMapper {

    public Comment toEntity(CreateCommentDto dto, User user, MediaContent mediaContent, Comment parentComment) {
        if (dto == null) return null;

        Comment comment = new Comment(
                null,
                user,
                mediaContent,
                dto.getText(),
                null,
                parentComment,
                new ArrayList<>(),
                0
        );

        return comment;
    }

    public CommentDto toDto(Comment comment) {
        if (comment == null) return null;

        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setMediaContentId(comment.getMediaContent() != null ? comment.getMediaContent().getId() : null);
        dto.setParentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null);
        dto.setText(comment.getContent());
        dto.setAuthor(comment.getUser() != null ? comment.getUser().getName() : null);
        dto.setCreatedAt(comment.getCreatedAt() != null ? comment.getCreatedAt().atOffset(ZoneOffset.UTC) : null);

        if (comment.getParentComment() == null) {
            dto.setChildrenCount(comment.getChildrenCount());
        } else {
            dto.setChildrenCount(null);
        }

        return dto;
    }

    public CommentDto toDtoWithChildren(Comment comment) {
        if (comment == null) return null;

        CommentDto dto = toDto(comment);

        if (comment.getChildrenComments() != null && !comment.getChildrenComments().isEmpty()) {
            List<CommentDto> childrenDtos = comment.getChildrenComments().stream()
                    .map(this::toDtoWithChildren)
                    .toList();
            dto.setChildren(childrenDtos);
        } else {
            dto.setChildren(Collections.emptyList());
        }

        return dto;
    }

    private void fillChildren(CommentDto dto, Map<Long, List<CommentDto>> childrenMap) {
        List<CommentDto> children = childrenMap.get(dto.getId());
        if (children != null && !children.isEmpty()) {
            dto.setChildren(children);
            for (CommentDto child : children) {
                fillChildren(child, childrenMap);
            }
        }
    }

}
