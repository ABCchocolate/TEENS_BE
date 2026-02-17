package kusuri12.teens_be.domain.information.presentation.dto.request;

public record UpdateInformationRequest (
        String title,
        String content
) { }