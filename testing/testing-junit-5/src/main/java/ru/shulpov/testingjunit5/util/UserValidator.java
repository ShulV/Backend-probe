package ru.shulpov.testingjunit5.util;

import ru.shulpov.testingjunit5.dto.ValidationResult;
import ru.shulpov.testingjunit5.entity.User;

/**
 * @author Shulpov Victor, E-Mail: shulpov.v@soft-logic.team
 * Created on 20.01.2026 15:36
 */
public class UserValidator {

    public boolean isValid(User user) {
        if (user == null) {
            return false;
        }

        boolean emailValid = user.getEmail() != null
                && !user.getEmail().isBlank()
                && user.getEmail().matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");

        boolean ageValid = user.getAge() >= 18 && user.getAge() <= 120;

        return emailValid && ageValid;
    }

    // Дополнительно: метод для детальной валидации
    public ValidationResult validate(User user) {
        ValidationResult result = new ValidationResult();

        if (user == null) {
            result.addError("user", "User cannot be null");
            return result;
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            result.addError("email", "Email cannot be null or blank");
        } else if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")) {
            result.addError("email", "Invalid email format");
        }

        if (user.getAge() < 18 || user.getAge() > 120) {
            result.addError("age", "Age must be between 18 and 120");
        }

        return result;
    }
}

