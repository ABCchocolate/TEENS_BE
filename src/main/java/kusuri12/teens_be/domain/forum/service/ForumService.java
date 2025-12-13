package kusuri12.teens_be.domain.forum.service;

import kusuri12.teens_be.domain.comment.domain.Comment;
import kusuri12.teens_be.domain.comment.domain.repository.CommentRepository;
import kusuri12.teens_be.domain.comment.presentation.dto.response.CommentResponse;
import kusuri12.teens_be.domain.forum.exception.ForumNotFoundException;
import kusuri12.teens_be.domain.forum.presentation.dto.request.CreateForumRequest;
import kusuri12.teens_be.domain.forum.presentation.dto.request.UpdateForumRequest;
import kusuri12.teens_be.domain.forum.presentation.dto.response.ForumDetailResponse;
import kusuri12.teens_be.domain.forum.presentation.dto.response.ForumListResponse;
import kusuri12.teens_be.domain.forum.domain.Forum;
import kusuri12.teens_be.domain.forum.domain.repository.ForumRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.domain.repository.UserRepository;
import kusuri12.teens_be.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ForumService {

    private final ForumRepository forumRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private Long userId;

    public List<ForumListResponse> getAllForums() {
        List<Forum> forums = forumRepository.findAllOrderByCreatedAtDesc();

        return forums.stream()
                .map(forum -> ForumListResponse.builder()
                        .id(forum.getId())
                        .title(forum.getTitle())
                        .authorName(forum.getUser().getNickname())
                        .createdAt(forum.getCreatedAt())
                        .commentCount(commentRepository.countByForumId(forum.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    public ForumDetailResponse getForumDetail(Long forumId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> ForumNotFoundException.EXCEPTION);

        List<Comment> comments = commentRepository.findByForumIdOrderByCreatedAtAsc(forumId);

        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .authorName(comment.getUser().getNickname())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return ForumDetailResponse.builder()
                .id(forum.getId())
                .title(forum.getTitle())
                .content(forum.getContent())
                .authorName(forum.getUser().getNickname())
                .createdAt(forum.getCreatedAt())
                .comments(commentResponses)
                .build();
    }

    public void createForum(CreateForumRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        Forum forum = Forum.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        forumRepository.save(forum);

        // ForumCount 증가
        user.increaseForumCount();
    }

    public void updateForum(Long forumId, UpdateForumRequest request) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> ForumNotFoundException.EXCEPTION);

        forum.updateTitleAndContent(request.getTitle(), request.getContent());
    }

    public void deleteForum(Long forumId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> ForumNotFoundException.EXCEPTION);

        // ForumCount 감소
        User user = forum.getUser();
        user.decreaseForumCount();

        forumRepository.delete(forum);
    }
}