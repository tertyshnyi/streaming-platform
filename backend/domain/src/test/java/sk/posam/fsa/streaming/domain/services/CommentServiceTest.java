package sk.posam.fsa.streaming.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.posam.fsa.streaming.domain.models.entities.Comment;
import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.repositories.CommentRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    private CommentRepository commentRepository;
    private UnifiedMediaContentFacade mediaFacade;
    private CommentService commentService;

    @BeforeEach
    void setup() {
        commentRepository = mock(CommentRepository.class);
        mediaFacade = mock(UnifiedMediaContentFacade.class);
        commentService = new CommentService(commentRepository, mediaFacade);
    }

    @Test
    void createComment_validComment_returnsSavedComment() {
        User user = new User();
        MediaContent media = new Movie(); // Используем подкласс MediaContent
        media.setId(1L);

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setMediaContent(media);
        comment.setContent("Test comment");

        when(commentRepository.create(any())).thenReturn(comment);

        Comment result = commentService.createComment(comment);

        assertNotNull(result);
        verify(mediaFacade).incrementCommentsTotal(1L);
        verify(commentRepository).create(any());
    }

    @Test
    void createComment_nullContent_throwsException() {
        Comment comment = new Comment();
        comment.setUser(new User());
        comment.setMediaContent(new Movie());

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(comment));
    }

    @Test
    void createComment_nullUser_throwsException() {
        Comment comment = new Comment();
        comment.setMediaContent(new Movie());
        comment.setContent("Some content");

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(comment));
    }

    @Test
    void createComment_nullMediaContent_throwsException() {
        Comment comment = new Comment();
        comment.setUser(new User());
        comment.setContent("Some content");

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(comment));
    }
}
