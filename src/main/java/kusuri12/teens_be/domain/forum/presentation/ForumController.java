package kusuri12.teens_be.domain.forum.presentation;

import kusuri12.teens_be.domain.comment.service.CommentService;
import kusuri12.teens_be.domain.forum.presentation.request.dto.ForumDto;
import kusuri12.teens_be.domain.forum.service.ForumService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<ForumDto.ForumListResponse>> getAllForums() {
        return ResponseEntity.ok(forumService.getAllForums());
    }

    @GetMapping("/{forumId}")
    public ResponseEntity<ForumDto.ForumDetailResponse> getForumDetail(@PathVariable Long forumId) {
        return ResponseEntity.ok(forumService.getForumDetail(forumId));
    }

    @PostMapping
    public ResponseEntity<Void> createForum(@RequestBody ForumDto.CreateForumRequest request) {
        forumService.createForum(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{forumId}/comments")
    public ResponseEntity<Void> createComment(
            @PathVariable Long forumId,
            @RequestBody ForumDto.CreateCommentRequest request) {
        commentService.createComment(forumId, request);
        return ResponseEntity.ok().build();
    }
}