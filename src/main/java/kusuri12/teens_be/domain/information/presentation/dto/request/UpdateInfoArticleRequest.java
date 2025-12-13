package kusuri12.teens_be.domain.information.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInfoArticleRequest {
    private String title;
    private String content;
    private boolean pinned;
    private String imageUrl;
}