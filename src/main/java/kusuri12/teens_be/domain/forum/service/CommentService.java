package kusuri12.teens_be.domain.forum.service;

import kusuri12.teens_be.domain.forum.domain.Comment;
import kusuri12.teens_be.domain.forum.exception.ForumErrorCode;
import kusuri12.teens_be.domain.forum.presentation.dto.response.CommentResponse;
import kusuri12.teens_be.domain.forum.repository.CommentRepository;
import kusuri12.teens_be.domain.forum.presentation.dto.request.CreateCommentRequest;
import kusuri12.teens_be.domain.forum.presentation.dto.request.UpdateCommentRequest;
import kusuri12.teens_be.domain.forum.domain.Forum;
import kusuri12.teens_be.domain.forum.repository.ForumRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.global.error.exception.GlobalErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;
import kusuri12.teens_be.global.security.annotation.CheckAuthor;
import kusuri12.teens_be.global.security.annotation.CheckId;
import kusuri12.teens_be.global.security.aspect.Authorizable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService implements Authorizable {

    private final CommentRepository commentRepository;
    private final ForumRepository forumRepository;
    private final UserRepository userRepository;

    // comment 조회
    @Transactional(readOnly = true)
    public Slice<CommentResponse> getComment(Long forumId, Pageable pageable) {
        if (!forumRepository.existsById(forumId)) {
            throw new TeensException(ForumErrorCode.FORUM_NOT_FOUND);
        }

        // 댓글 가져오기
        Slice<Comment> comments = commentRepository.findByForumIdOrderByCreatedAtAsc(forumId, pageable);

        // 게시글 정보 반환
        return comments.map(CommentResponse::from);
    }


    @Transactional
    public void createComment(Long userId, Long forumId, CreateCommentRequest request) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new TeensException(ForumErrorCode.FORUM_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TeensException(UserErrorCode.USER_NOT_FOUND));

        Comment comment = Comment.of(request.content(), forum, user);

        commentRepository.save(comment);

        // CommentCount 증가
        user.increaseCommentCount();
    }

    @Transactional
    @CheckAuthor
    public void updateComment(Long forumId, @CheckId Long commentId, UpdateCommentRequest request) {
        Comment comment = getValidatedComment(forumId, commentId);
        comment.updateContent(request.content());
    }

    @Transactional
    @CheckAuthor
    public void deleteComment(Long forumId, @CheckId Long commentId) {
        Comment comment = getValidatedComment(forumId, commentId);

        // CommentCount 감소
        User user = comment.getUser();
        user.decreaseCommentCount();

        commentRepository.delete(comment);
    }

    private Comment getValidatedComment(Long forumId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TeensException(ForumErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getForum().getId().equals(forumId)) {
            throw new TeensException(GlobalErrorCode.RESOURCE_MISMATCH);
        }

        return comment;
    }

    @Override
    public Long getAuthorId(Long resourceId) {
        return commentRepository.findById(resourceId)
                .map(comment -> comment.getUser().getId())
                .orElseThrow(() -> new TeensException(ForumErrorCode.COMMENT_NOT_FOUND));
    }
}