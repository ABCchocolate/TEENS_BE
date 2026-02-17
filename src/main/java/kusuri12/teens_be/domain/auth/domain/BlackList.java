package kusuri12.teens_be.domain.auth.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash(value = "auth:black_list")
public class BlackList {

    @Id
    private String accessToken;

    @Indexed
    private String username;

    @TimeToLive
    private Long ttl;
}
