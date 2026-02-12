package kusuri12.teens_be.domain.forum.presentation.dto.request;

public record CreateForumRequest (
    String title,
    String content
) { }