package kusuri12.teens_be.domain.forum.service;

import kusuri12.teens_be.domain.forum.domain.Comment;
import kusuri12.teens_be.domain.forum.exception.ForumErrorCode;
import kusuri12.teens_be.domain.forum.presentation.dto.response.CommentResponse;
import kusuri12.teens_be.domain.forum.repository.CommentRepository;
import kusuri12.teens_be.domain.forum.presentation.dto.request.CreateForumRequest;
import kusuri12.teens_be.domain.forum.presentation.dto.request.UpdateForumRequest;
import kusuri12.teens_be.domain.forum.presentation.dto.response.ForumDetailResponse;
import kusuri12.teens_be.domain.forum.presentation.dto.response.ForumListResponse;
import kusuri12.teens_be.domain.forum.domain.Forum;
import kusuri12.teens_be.domain.forum.repository.ForumRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.global.error.exception.TeensException;
import kusuri12.teens_be.global.security.annotation.CheckAuthor;
import kusuri12.teens_be.global.security.annotation.CheckId;
import kusuri12.teens_be.global.security.aspect.Authorizable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForumService implements Authorizable {

    private final ForumRepository forumRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<ForumListResponse> getAllForums(Pageable pageable) {
        Page<Forum> forums = forumRepository.findAllOrderByCreatedAtDesc(pageable);
        return convertResponsePage(forums);
    }

    @Transactional(readOnly = true)
    public Page<ForumListResponse> searchForums(Pageable pageable, String keyword) {
        Page<Forum> forums = forumRepository.searchByKeyword(keyword, pageable);
        return convertResponsePage(forums);
    }

    private Page<ForumListResponse> convertResponsePage(Page<Forum> forums) {
        return forums.map(forum -> {
            Long commentCount = commentRepository.countByForumId(forum.getId());
            return ForumListResponse.of(forum, commentCount);
        });
    }

    @Transactional(readOnly = true)
    public ForumDetailResponse getForumDetail(Long forumId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new TeensException(ForumErrorCode.FORUM_NOT_FOUND));

        // 게시글 정보 반환
        return ForumDetailResponse.from(forum);
    }

    @Transactional
    public void createForum(Long userId, CreateForumRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TeensException(UserErrorCode.USER_NOT_FOUND));

        Forum forum = Forum.of(request.title(), request.content(), user);

        forumRepository.save(forum);

        user.increaseForumCount();
    }

    @Transactional
    @CheckAuthor
    public void updateForum(@CheckId Long forumId, UpdateForumRequest request) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new TeensException(ForumErrorCode.FORUM_NOT_FOUND));

        forum.updateTitleAndContent(request.title(), request.content());
    }

    @Transactional
    @CheckAuthor
    public void deleteForum(@CheckId Long forumId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new TeensException(ForumErrorCode.FORUM_NOT_FOUND));

        User user = forum.getUser();
        user.decreaseForumCount();

        forumRepository.delete(forum);
    }

    @Override
    public Long getAuthorId(Long resourceId) {
        return forumRepository.findById(resourceId)
                .map(forum -> forum.getUser().getId())
                .orElseThrow(() -> new TeensException(ForumErrorCode.FORUM_NOT_FOUND));
    }
}