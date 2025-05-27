package sk.posam.fsa.streaming.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.posam.fsa.streaming.domain.models.entities.Comment;

import java.util.List;

public interface CommentSpringDataRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMediaContentId(Long mediaContentId);
    List<Comment> findByParentComment(Comment parentComment);

}
