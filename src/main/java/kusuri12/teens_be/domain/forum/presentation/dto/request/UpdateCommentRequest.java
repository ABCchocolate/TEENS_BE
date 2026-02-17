package kusuri12.teens_be.domain.forum.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kusuri12.teens_be.global.validation.anotation.Ban;

public record UpdateCommentRequest(

        @NotBlank
        @Size(max = 1000)
        @Ban
        String content
) { }