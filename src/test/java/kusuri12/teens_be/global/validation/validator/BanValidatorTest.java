package kusuri12.teens_be.global.validation.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BanValidatorTest {

    @BeforeEach
    void setUp() {
        BanValidator validator = new BanValidator();
        validator.initialize(null);
    }

    @Test
    void isValid_Success() {
        BanValidator validator = new BanValidator();

        String cleanText = "안녕하세요 승리님! 오늘 날씨가 참 좋네요.";

        // when
        boolean result = validator.isValid(cleanText, null);

        // then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"이런 시바", "개새끼야", "미친놈"})
    void isValid_Fail(String badText) {
        BanValidator validator = new BanValidator();

        // when
        boolean result = validator.isValid(badText, null);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void isValid_Null() {
        BanValidator validator = new BanValidator();

        // when
        boolean result = validator.isValid(null, null);

        // then
        assertThat(result).isTrue();
    }
}