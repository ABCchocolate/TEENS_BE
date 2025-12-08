package kusuri12.teens_be.domain.forum.service;

import kusuri12.teens_be.domain.comment.domain.Comment;
import kusuri12.teens_be.domain.comment.domain.repository.CommentRepository;
import kusuri12.teens_be.domain.forum.presentation.request.dto.ForumDto;
import kusuri12.teens_be.domain.forum.domain.Forum;
import kusuri12.teens_be.domain.forum.domain.repository.ForumRepository;
import kusuri12.teens_be.domain.user.entity.User;
import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForumService {

    private final ForumRepository forumRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public List<ForumDto.ForumListResponse> getAllForums() {
        List<Forum> forums = forumRepository.findAllOrderByCreatedAtDesc();

        return forums.stream()
                .map(forum -> ForumDto.ForumListResponse.builder()
                        .id(forum.getId())
                        .title(forum.getTitle())
                        .authorName(forum.getUser().getNickname())
                        .createdAt(forum.getCreatedAt())
                        .commentCount(commentRepository.countByForumId(forum.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    public ForumDto.ForumDetailResponse getForumDetail(Long forumId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new CustomException(ErrorCode.FORUM_NOT_FOUND));

        List<Comment> comments = commentRepository.findByForumIdOrderByCreatedAtAsc(forumId);

        List<ForumDto.CommentResponse> commentResponses = comments.stream()
                .map(comment -> ForumDto.CommentResponse.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .authorName(comment.getUser().getNickname())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return ForumDto.ForumDetailResponse.builder()
                .id(forum.getId())
                .title(forum.getTitle())
                .content(forum.getContent())
                .authorName(forum.getUser().getNickname())
                .createdAt(forum.getCreatedAt())
                .comments(commentResponses)
                .build();
    }

    @Transactional
    public void createForum(ForumDto.CreateForumRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Forum forum = Forum.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        forumRepository.save(forum);
    }
}