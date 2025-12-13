package kusuri12.teens_be.domain.information.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateInfoArticleRequest {
    private String title;
    private String content;
    private Long userId;
    private boolean pinned;
    private String imageUrl;
}