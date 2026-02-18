package kusuri12.teens_be.domain.information.presentation.dto.response;

import kusuri12.teens_be.domain.information.domain.Information;

import java.time.LocalDateTime;

public record InformationListResponse (
        Long id,
        String title,
        String authorName,
        LocalDateTime createdAt
) {
    public static InformationListResponse of(Information information) {
        return new InformationListResponse(
                information.getId(),
                information.getTitle(),
                information.getUser().getNickname(),
                information.getCreatedAt()
        );
    }
}