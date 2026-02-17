package kusuri12.teens_be.domain.auth.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash(value = "auth:refresh_token")
public class RefreshToken {

    @Id
    private String username;

    private String token;

    @TimeToLive
    private Long expireAt;
}
