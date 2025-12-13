package kusuri12.teens_be.domain.comment.service;

import kusuri12.teens_be.domain.comment.domain.Comment;
import kusuri12.teens_be.domain.comment.domain.repository.CommentRepository;
import kusuri12.teens_be.domain.comment.exception.CommentNotFoundException;
import kusuri12.teens_be.domain.comment.presentation.dto.request.CreateCommentRequest;
import kusuri12.teens_be.domain.comment.presentation.dto.request.UpdateCommentRequest;
import kusuri12.teens_be.domain.forum.exception.ForumNotFoundException;
import kusuri12.teens_be.domain.forum.domain.Forum;
import kusuri12.teens_be.domain.forum.domain.repository.ForumRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.domain.repository.UserRepository;
import kusuri12.teens_be.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final ForumRepository forumRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createComment(Long forumId, CreateCommentRequest request) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> ForumNotFoundException.EXCEPTION);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        Comment comment = Comment.builder()
                .content(request.getContent())
                .forum(forum)
                .user(user)
                .build();

        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long commentId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> CommentNotFoundException.EXCEPTION);

        comment.updateContent(request.getContent());
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> CommentNotFoundException.EXCEPTION);

        commentRepository.delete(comment);
    }
}