package kusuri12.teens_be.global.aws;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.cloud.aws")
public class AwsProperties {

    private final S3 s3;
    private final Credentials credentials;
    private final Region region;

    public record S3(
            String bucket) { }

    public record Credentials(
            String accessKey,
            String secretKey) { }

    public record Region(
            @Name("static") String staticRegion
    ) { }
}