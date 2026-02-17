package kusuri12.teens_be.global.config;

import kusuri12.teens_be.global.aws.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final AwsProperties awsProperties;

    @Bean
    public S3Client S3Client() {
        // 1. 자격 증명 객체 생성 (AwsBasicCredentials 사용)
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                awsProperties.getCredentials().accessKey(),
                awsProperties.getCredentials().secretKey());

        // 2. S3Client Builder를 사용하여 클라이언트 생성
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(awsProperties.getRegion().staticRegion()))
                .build();
    }
}