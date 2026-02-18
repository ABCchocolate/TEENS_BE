package kusuri12.teens_be.domain.information.presentation.dto.response;

import kusuri12.teens_be.domain.information.domain.Information;
import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import kusuri12.teens_be.global.validation.util.FieldUtil;

import java.time.LocalDateTime;

public record InformationDetailResponse (
        Long id,
        String title,
        String content,
        String authorName,
        LocalDateTime createdAt
) {
    public static InformationDetailResponse from(Information information) {
        return new InformationDetailResponse(
                information.getId(),
                information.getTitle(),
                information.getContent(),
                FieldUtil.notNull(information.getUser(), UserErrorCode.USER_NOT_FOUND).getNickname(),
                information.getCreatedAt()
        );
    }
}