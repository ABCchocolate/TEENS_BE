package kusuri12.teens_be.domain.information.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class InfoArticleDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoArticleListResponse {
        private Long id;
        private String title;
        private String authorName;
        private LocalDateTime createdAt;
        private boolean pinned;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoArticleDetailResponse {
        private Long id;
        private String title;
        private String content;
        private String authorName;
        private LocalDateTime createdAt;
        private String imageUrl;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateInfoArticleRequest {
        private String title;
        private String content;
        private Long userId;
        private boolean pinned;
        private String imageUrl;
    }
}