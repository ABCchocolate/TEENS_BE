package kusuri12.teens_be.domain.information.presentation.dto.request;

import lombok.Getter;

@Getter
public class UpdateInfoArticleRequest {
    private String title;
    private String content;
    private boolean pinned;
    private String imageUrl;
}