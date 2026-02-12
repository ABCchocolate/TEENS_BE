package kusuri12.teens_be;

import kusuri12.teens_be.global.jwt.JwtProperties;
import kusuri12.teens_be.global.aws.AwsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, AwsProperties.class})
public class TeensBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeensBeApplication.class, args);
    }

}
