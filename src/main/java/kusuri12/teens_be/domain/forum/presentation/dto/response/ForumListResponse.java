package kusuri12.teens_be.domain.forum.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForumListResponse {
    private Long id;
    private String title;
    private String authorName;
    private LocalDateTime createdAt;
    private Long commentCount;
}