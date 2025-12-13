package kusuri12.teens_be.domain.information.presentation.dto.request;

import lombok.Getter;

@Getter
public class CreateInfoArticleRequest {
    private String title;
    private String content;
    private Long userId;
    private boolean pinned;
    private String imageUrl;
}