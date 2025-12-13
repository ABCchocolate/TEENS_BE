package kusuri12.teens_be.domain.forum.presentation.dto.response;

import kusuri12.teens_be.domain.comment.presentation.dto.response.CommentResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ForumDetailResponse {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
    private List<CommentResponse> comments;
}