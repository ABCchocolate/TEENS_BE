package kusuri12.teens_be.domain.information.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoArticleListResponse {
    private Long id;
    private String title;
    private String authorName;
    private LocalDateTime createdAt;
    private boolean pinned;
}