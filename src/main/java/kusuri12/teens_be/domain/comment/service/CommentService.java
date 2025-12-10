package kusuri12.teens_be.domain.comment.service;

import kusuri12.teens_be.domain.comment.domain.Comment;
import kusuri12.teens_be.domain.comment.domain.repository.CommentRepository;
import kusuri12.teens_be.domain.forum.domain.Forum;
import kusuri12.teens_be.domain.forum.domain.repository.ForumRepository;
import kusuri12.teens_be.domain.forum.presentation.request.dto.ForumDto;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.global.error.exception.ErrorCode;
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
    public void createComment(Long forumId, ForumDto.CreateCommentRequest request) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new CustomException(ErrorCode.FORUM_NOT_FOUND));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .forum(forum)
                .user(user)
                .build();

        commentRepository.save(comment);
    }
}
