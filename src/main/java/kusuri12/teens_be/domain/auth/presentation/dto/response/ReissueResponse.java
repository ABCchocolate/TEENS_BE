package kusuri12.teens_be.domain.auth.presentation.dto.response;

public record ReissueResponse(
        String accessToken,
        String refreshToken
) {
    public static ReissueResponse of(String accessToken, String refreshToken) {
        return new ReissueResponse(accessToken, refreshToken);
    }
}
