package kusuri12.teens_be.domain.forum.service;

import kusuri12.teens_be.domain.forum.domain.Comment;
import kusuri12.teens_be.domain.forum.repository.CommentRepository;
import kusuri12.teens_be.domain.forum.presentation.dto.request.CreateForumRequest;
import kusuri12.teens_be.domain.forum.presentation.dto.request.UpdateForumRequest;
import kusuri12.teens_be.domain.forum.presentation.dto.response.ForumDetailResponse;
import kusuri12.teens_be.domain.forum.presentation.dto.response.ForumListResponse;
import kusuri12.teens_be.domain.forum.domain.Forum;
import kusuri12.teens_be.domain.forum.repository.ForumRepository;
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
public class ForumService {

    private final ForumRepository forumRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
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

    @Transactional
    public List<ForumListResponse> searchForums(String keyword) {
        List<Forum> forums = forumRepository.searchByKeyword(keyword);

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

    @Transactional
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

    @Transactional
    public void createForum(Long userId, CreateForumRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        Forum forum = Forum.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        forumRepository.save(forum);

        user.increaseForumCount();
    }

    @Transactional
    public void updateForum(Long forumId, UpdateForumRequest request) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> ForumNotFoundException.EXCEPTION);

        forum.updateTitleAndContent(request.getTitle(), request.getContent());
    }

    @Transactional
    public void deleteForum(Long forumId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> ForumNotFoundException.EXCEPTION);

        User user = forum.getUser();
        user.decreaseForumCount();

        forumRepository.delete(forum);
    }
}