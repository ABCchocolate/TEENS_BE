package kusuri12.teens_be.domain.forum.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kusuri12.teens_be.global.validation.anotation.Ban;

public record UpdateForumRequest (

        @NotBlank
        @Size(max = 100)
        @Ban
        String title,

        @NotBlank
        @Size(max = 2000)
        @Ban
        String content
) { }