package sk.posam.fsa.streaming.jpa;

import org.springframework.stereotype.Repository;
import sk.posam.fsa.streaming.domain.models.entities.Comment;
import sk.posam.fsa.streaming.domain.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCommentRepositoryAdapter implements CommentRepository {

    private final CommentSpringDataRepository springDataRepository;

    public JpaCommentRepositoryAdapter(CommentSpringDataRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Optional<Comment> get(Long id) {
        return springDataRepository.findById(id);
    }

    @Override
    public Comment create(Comment comment) {
        return springDataRepository.save(comment);
    }

    @Override
    public Comment update(Comment comment) {
        return springDataRepository.save(comment);
    }

    @Override
    public void delete(Long id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public List<Comment> findByMediaContentId(Long mediaContentId) {
        return springDataRepository.findByMediaContentId(mediaContentId);
    }
    @Override
    public List<Comment> findByParentComment(Comment parentComment) {
        return springDataRepository.findByParentComment(parentComment);
    }
}
