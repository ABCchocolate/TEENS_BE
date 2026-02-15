package kusuri12.teens_be.domain.information.presentation.dto.request;

public record CreateInformationRequest (
        String title,
        String content
) { }