package ru.shulpov.streamapi;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class StreamTasks {
    private StreamTasks() {
    }

    /**
     * Нормализовать слова: убрать пробелы по краям, привести к lowercase,
     * удалить пустые строки и дубликаты, сохранив порядок первого появления.
     */
    public static List<String> normalizeAndDeduplicateWords(List<String> words) {
        return words.stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .distinct()
                .filter(el -> !el.isEmpty())
                .toList();
    }

    public static List<String> topNFrequentWords(List<String> words, int limit) {
        throw new UnsupportedOperationException("Task 2 is not implemented yet");
    }

    public static Map<String, List<String>> activeEmployeeNamesByDepartment(List<Employee> employees) {
        throw new UnsupportedOperationException("Task 3 is not implemented yet");
    }

    public static Map<String, BigDecimal> averageSalaryByDepartment(List<Employee> employees) {
        throw new UnsupportedOperationException("Task 4 is not implemented yet");
    }

    public static Optional<Employee> secondHighestPaidEmployee(List<Employee> employees) {
        throw new UnsupportedOperationException("Task 5 is not implemented yet");
    }

    public static Map<String, BigDecimal> totalAmountByCustomer(List<Order> orders) {
        throw new UnsupportedOperationException("Task 6 is not implemented yet");
    }

    public static Map<String, Product> mostExpensiveProductByCategory(List<Product> products) {
        throw new UnsupportedOperationException("Task 7 is not implemented yet");
    }

    public static Map<String, Transaction> lastTransactionByAccount(List<Transaction> transactions) {
        throw new UnsupportedOperationException("Task 8 is not implemented yet");
    }

    public static Map<LogLevel, Long> countLogLevels(List<String> lines) {
        throw new UnsupportedOperationException("Task 9 is not implemented yet");
    }

    public static List<String> topTags(List<Article> articles, int limit) {
        throw new UnsupportedOperationException("Task 10 is not implemented yet");
    }
}
