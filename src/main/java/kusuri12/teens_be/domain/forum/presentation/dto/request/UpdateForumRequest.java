package kusuri12.teens_be.domain.forum.presentation.dto.request;

import lombok.Getter;

@Getter
public class UpdateForumRequest {
    private String title;
    private String content;
}