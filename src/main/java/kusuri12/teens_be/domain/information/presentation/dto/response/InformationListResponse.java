package kusuri12.teens_be.domain.information.presentation.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record InformationListResponse (
        Long id,
        String title,
        String authorName,
        LocalDateTime createdAt
) { }