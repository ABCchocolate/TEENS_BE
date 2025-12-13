package kusuri12.teens_be.domain.comment.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {
    private String content;
    private Long userId;
}