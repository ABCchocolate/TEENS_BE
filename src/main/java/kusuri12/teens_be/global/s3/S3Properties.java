package kusuri12.teens_be.global.s3;

import jdk.jfr.Name;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "cloud.aws")
public class S3Properties {

    private final S3 s3;
    private final Credentials credentials;
    private final Region region;

    public record S3(
            String bucket) { }

    public record Credentials(
            String accessKey,
            String secretKey) { }

    public record Region(
            @Name("static") String staticRegion) { }
}