package kusuri12.teens_be.domain.information.presentation.dto.response;

import kusuri12.teens_be.domain.information.domain.Information;
import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import kusuri12.teens_be.global.validation.util.FieldUtil;

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
                FieldUtil.notNull(information.getUser(), UserErrorCode.USER_NOT_FOUND).getNickname(),
                information.getCreatedAt()
        );
    }
}