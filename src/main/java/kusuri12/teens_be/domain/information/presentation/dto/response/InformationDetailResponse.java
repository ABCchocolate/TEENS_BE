package kusuri12.teens_be.domain.information.presentation.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record InformationDetailResponse (
        Long id,
        String title,
        String content,
        String authorName,
        LocalDateTime createdAt
) { }