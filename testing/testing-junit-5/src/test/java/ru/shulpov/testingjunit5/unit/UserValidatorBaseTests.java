package ru.shulpov.testingjunit5.unit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.shulpov.testingjunit5.dto.ValidationResult;
import ru.shulpov.testingjunit5.entity.User;
import ru.shulpov.testingjunit5.util.UserValidator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

public class UserValidatorBaseTests {
    private final UserValidator validator = new UserValidator();

    @BeforeEach
    void setUp() {
        //выполнение перед каждым тестом
    }

    @BeforeAll
    public static void init() {
        //выполнение перед всеми тестами
    }

    @ParameterizedTest(name = "Валидный пользователь: email={0}, возраст={1}")
    @DisplayName("✅ Должен успешно валидировать корректных пользователей")
    @CsvSource({
            "test@example.com, 18",
            "user+tag@domain.ru, 25",
            "user.name@sub.domain.com, 45",
            "a@b.co, 120"
    })
    void shouldValidateValidUsers(String email, int age) {
        User user = new User(email, age);

        boolean result = validator.isValid(user);

        assertThat(result).as("Пользователь %s (%d лет) должен быть валиден", email, age)
                .isTrue();
    }

    @Nested
    @DisplayName("❌ Невалидный email")
    class InvalidEmailTests {

        @ParameterizedTest(name = "Email: {0}")
        @CsvSource({
                "invalid-email",
                "''",
                "' '",
                "no@at",
                "@domain.com",
                "test@domain"
        })
        void shouldRejectInvalidEmails(String email) {
            User user = new User(email, 30);

            boolean result = validator.isValid(user);

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("❌ Невалидный возраст")
    class InvalidAgeTests {

        @ParameterizedTest(name = "Возраст: {0}")
        @CsvSource({
                "17",
                "0",
                "-1",
                "121",
                "150"
        })
        void shouldRejectInvalidAges(int age) {
            User user = new User("test@example.com", age);

            boolean result = validator.isValid(user);

            assertThat(result).isFalse();
        }
    }

    @ParameterizedTest(name = "Детальная валидация: email={0}, возраст={1}")
    @DisplayName("🔍 validate() возвращает правильные ошибки")
    @CsvSource({
            "'invalid-email', 30",      // email ошибка
            "test@example.com, 17",     // age ошибка
            "'bad@', 17",               // обе ошибки
            "test@example.com, 30"      // валидно
    })
    void validateShouldReturnCorrectErrors(String email, int age) {
        User user = new User(email, age);
        ValidationResult result = validator.validate(user);

        assertThat(result.isValid())
                .as("Email: %s, возраст: %d", email, age)
                .isEqualTo(email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$") && age >= 18 && age <= 120);
    }

    @Test
    @DisplayName("Null user → ошибки на поле 'user'")
    void nullUserValidation() {
        ValidationResult result = validator.validate(null);

        assertThat(result.isValid()).isFalse();
        assertThat(result.getFieldErrors())
                .containsEntry("user", List.of("User cannot be null"))
                .hasSize(1);
    }

    @Test
    @DisplayName("Проверка теста с бросанием исключения")
    void nullUserForNotNullValidation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> validator.validateNotNullUser(null));
    }
}
