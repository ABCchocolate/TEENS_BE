package kusuri12.teens_be.domain.forum.presentation.dto.request;

import lombok.Getter;

@Getter
public class CreateForumRequest {
    private String title;
    private String content;
}