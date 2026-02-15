package kusuri12.teens_be.domain.forum.service;

import kusuri12.teens_be.domain.forum.domain.Comment;
import kusuri12.teens_be.domain.forum.exception.ForumErrorCode;
import kusuri12.teens_be.domain.forum.repository.CommentRepository;
import kusuri12.teens_be.domain.forum.presentation.dto.request.CreateCommentRequest;
import kusuri12.teens_be.domain.forum.presentation.dto.request.UpdateCommentRequest;
import kusuri12.teens_be.domain.forum.domain.Forum;
import kusuri12.teens_be.domain.forum.repository.ForumRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.global.error.exception.TeensException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ForumRepository forumRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createComment(Long userId, Long forumId, CreateCommentRequest request) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new TeensException(ForumErrorCode.FORUM_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TeensException(UserErrorCode.USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.content())
                .forum(forum)
                .user(user)
                .build();

        commentRepository.save(comment);

        // CommentCount 증가
        user.increaseCommentCount();
    }

    @Transactional
    public void updateComment(Long commentId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TeensException(ForumErrorCode.COMMENT_NOT_FOUND));

        comment.updateContent(request.content());
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TeensException(ForumErrorCode.COMMENT_NOT_FOUND));

        // CommentCount 감소
        User user = comment.getUser();
        user.decreaseCommentCount();

        commentRepository.delete(comment);
    }
}