package kusuri12.teens_be;

import kusuri12.teens_be.global.security.jwt.JwtProperties;
import kusuri12.teens_be.global.aws.AwsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({JwtProperties.class, AwsProperties.class})
public class TeensBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeensBeApplication.class, args);
    }

}
