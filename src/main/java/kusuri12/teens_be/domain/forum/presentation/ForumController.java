package kusuri12.teens_be.domain.forum.presentation;

import kusuri12.teens_be.domain.comment.presentation.dto.request.CreateCommentRequest;
import kusuri12.teens_be.domain.comment.presentation.dto.request.UpdateCommentRequest;
import kusuri12.teens_be.domain.comment.service.CommentService;
import kusuri12.teens_be.domain.forum.presentation.dto.request.CreateForumRequest;
import kusuri12.teens_be.domain.forum.presentation.dto.request.UpdateForumRequest;
import kusuri12.teens_be.domain.forum.presentation.dto.response.ForumDetailResponse;
import kusuri12.teens_be.domain.forum.presentation.dto.response.ForumListResponse;
import kusuri12.teens_be.domain.forum.service.ForumService;
import kusuri12.teens_be.global.auth.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<ForumListResponse>> getAllForums(
            @RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return ResponseEntity.ok(forumService.searchForums(keyword));
        }
        return ResponseEntity.ok(forumService.getAllForums());
    }

    @GetMapping("/{forum_id}")
    public ResponseEntity<ForumDetailResponse> getForumDetail(@PathVariable Long forum_id) {
        return ResponseEntity.ok(forumService.getForumDetail(forum_id));
    }

    @PostMapping
    public ResponseEntity<Void> createForum(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody CreateForumRequest request) {
        Long userId = authDetails.getId();
        forumService.createForum(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{forum_id}")
    public ResponseEntity<Void> updateForum(
            @PathVariable Long forum_id,
            @RequestBody UpdateForumRequest request) {
        forumService.updateForum(forum_id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{forum_id}")
    public ResponseEntity<Void> deleteForum(@PathVariable Long forum_id) {
        forumService.deleteForum(forum_id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{forum_id}/comment")
    public ResponseEntity<Void> createComment(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long forum_id,
            @RequestBody CreateCommentRequest request) {
//        System.out.println(forum_id);
        Long userId = authDetails.getId();
        commentService.createComment(userId, forum_id, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{forum_id}/comment/{comment_id}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long forum_id,
            @PathVariable Long comment_id,
            @RequestBody UpdateCommentRequest request) {
        commentService.updateComment(comment_id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{forum_id}/comment/{comment_id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long forum_id,
            @PathVariable Long comment_id) {
        commentService.deleteComment(comment_id);
        return ResponseEntity.noContent().build();
    }
}