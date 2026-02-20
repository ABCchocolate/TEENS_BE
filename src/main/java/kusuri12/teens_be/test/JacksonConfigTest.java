package kusuri12.teens_be.test;

import org.springframework.context.annotation.Profile;import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@Profile("test")
@RestController
@RequestMapping("/test")
class JacksonConfigTest {
    @GetMapping("/time")
    public Map<String, Object> timeTest() {
        // JSON의 날짜 형식: "yyyy-MM-dd HH:mm:ss"
        return Map.of("now", LocalDateTime.now());
    }
}