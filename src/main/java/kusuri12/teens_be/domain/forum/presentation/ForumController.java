package kusuri12.teens_be.domain.forum.presentation;

import kusuri12.teens_be.domain.comment.presentation.dto.request.CreateCommentRequest;
import kusuri12.teens_be.domain.comment.presentation.dto.request.UpdateCommentRequest;
import kusuri12.teens_be.domain.comment.service.CommentService;
import kusuri12.teens_be.domain.forum.presentation.dto.request.CreateForumRequest;
import kusuri12.teens_be.domain.forum.presentation.dto.request.UpdateForumRequest;
import kusuri12.teens_be.domain.forum.presentation.dto.response.ForumDetailResponse;
import kusuri12.teens_be.domain.forum.presentation.dto.response.ForumListResponse;
import kusuri12.teens_be.domain.forum.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<ForumListResponse>> getAllForums() {
        return ResponseEntity.ok(forumService.getAllForums());
    }

    @GetMapping("/{forumId}")
    public ResponseEntity<ForumDetailResponse> getForumDetail(@PathVariable Long forumId) {
        return ResponseEntity.ok(forumService.getForumDetail(forumId));
    }

    @PostMapping
    public ResponseEntity<Void> createForum(@RequestBody CreateForumRequest request) {
        forumService.createForum(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{forumId}")
    public ResponseEntity<Void> updateForum(
            @PathVariable Long forumId,
            @RequestBody UpdateForumRequest request) {
        forumService.updateForum(forumId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{forumId}")
    public ResponseEntity<Void> deleteForum(@PathVariable Long forumId) {
        forumService.deleteForum(forumId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{forumId}/comments")
    public ResponseEntity<Void> createComment(
            @PathVariable Long forumId,
            @RequestBody CreateCommentRequest request) {
        commentService.createComment(forumId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{forumId}/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long forumId,
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequest request) {
        commentService.updateComment(commentId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{forumId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long forumId,
            @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}