package kusuri12.teens_be.domain.forum.presentation.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ForumDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ForumListResponse {
        private Long id;
        private String title;
        private String authorName;
        private LocalDateTime createdAt;
        private Long commentCount;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ForumDetailResponse {
        private Long id;
        private String title;
        private String content;
        private String authorName;
        private LocalDateTime createdAt;
        private List<CommentResponse> comments;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentResponse {
        private Long id;
        private String content;
        private String authorName;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateForumRequest {
        private String title;
        private String content;
        private Long userId;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateCommentRequest {
        private String content;
        private Long userId;
    }
}