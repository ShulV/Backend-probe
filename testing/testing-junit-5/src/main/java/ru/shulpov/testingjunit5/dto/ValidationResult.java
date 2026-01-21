package ru.shulpov.testingjunit5.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidationResult {
    private final List<Error> errors = new ArrayList<>();

    public void addError(String field, String message) {
        errors.add(new Error(field, message));
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public List<Error> getErrors() {
        return new ArrayList<>(errors);
    }

    public Map<String, List<String>> getFieldErrors() {
        return errors.stream()
                .collect(Collectors.groupingBy(
                        Error::field,
                        Collectors.mapping(Error::message, Collectors.toList())
                ));
    }

    public record Error(String field, String message) {}
}

