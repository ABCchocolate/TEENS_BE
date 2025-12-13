package kusuri12.teens_be.domain.forum.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateForumRequest {
    private String title;
    private String content;
}