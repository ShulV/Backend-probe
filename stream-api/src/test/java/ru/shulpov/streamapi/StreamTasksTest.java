package ru.shulpov.streamapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class StreamTasksTest {
    @Test
    void normalizeAndDeduplicateWordsShouldTrimLowercaseRemoveBlankAndKeepFirstOrder() {
        List<String> result = StreamTasks.normalizeAndDeduplicateWords(List.of(
                " Java ", "stream", "JAVA", " ", "Collectors", "stream", "Optional", "optional "
        ));

        assertEquals(List.of("java", "stream", "collectors", "optional"), result);
    }

    @Test
    void topNFrequentWordsShouldSortByFrequencyDescThenAlphabetically() {
        List<String> result = StreamTasks.topNFrequentWords(List.of(
                "spring", "java", "kafka", "java", "stream", "spring", "java", "kafka", "docker"
        ), 3);

        assertEquals(List.of("java", "kafka", "spring"), result);
    }

    @Test
    void activeEmployeeNamesByDepartmentShouldGroupOnlyActiveEmployeesAndSortNamesBySalaryDesc() {
        List<Employee> employees = List.of(
                employee(1, "Ivan", "backend", "220000", true),
                employee(2, "Petr", "backend", "250000", true),
                employee(3, "Anna", "qa", "180000", true),
                employee(4, "Maria", "backend", "250000", true),
                employee(5, "Oleg", "qa", "190000", false),
                employee(6, "Elena", "qa", "210000", true)
        );

        Map<String, List<String>> result = StreamTasks.activeEmployeeNamesByDepartment(employees);

        assertEquals(List.of("Maria", "Petr", "Ivan"), result.get("backend"));
        assertEquals(List.of("Elena", "Anna"), result.get("qa"));
        assertEquals(2, result.size());
    }

    @Test
    void averageSalaryByDepartmentShouldUseOnlyActiveEmployees() {
        List<Employee> employees = List.of(
                employee(1, "Ivan", "backend", "200000", true),
                employee(2, "Petr", "backend", "300000", true),
                employee(3, "Anna", "qa", "150000", true),
                employee(4, "Oleg", "qa", "300000", false)
        );

        Map<String, BigDecimal> result = StreamTasks.averageSalaryByDepartment(employees);

        assertEquals(new BigDecimal("250000"), result.get("backend"));
        assertEquals(new BigDecimal("150000"), result.get("qa"));
    }

    @Test
    void secondHighestPaidEmployeeShouldUseSecondUniqueSalary() {
        List<Employee> employees = List.of(
                employee(1, "Ivan", "backend", "220000", true),
                employee(2, "Petr", "backend", "300000", true),
                employee(3, "Anna", "qa", "250000", true),
                employee(4, "Maria", "backend", "300000", true),
                employee(5, "Elena", "qa", "180000", true)
        );

        Optional<Employee> result = StreamTasks.secondHighestPaidEmployee(employees);

        assertTrue(result.isPresent());
        assertEquals("Anna", result.get().name());
    }

    @Test
    void totalAmountByCustomerShouldSumAllOrderItems() {
        List<Order> orders = List.of(
                order(1, "c-1", item("Keyboard", "5000", 2), item("Mouse", "2500", 1)),
                order(2, "c-2", item("Monitor", "30000", 1)),
                order(3, "c-1", item("Usb cable", "750", 4))
        );

        Map<String, BigDecimal> result = StreamTasks.totalAmountByCustomer(orders);

        assertEquals(new BigDecimal("15500"), result.get("c-1"));
        assertEquals(new BigDecimal("30000"), result.get("c-2"));
    }

    @Test
    void mostExpensiveProductByCategoryShouldResolvePriceTieByNameAsc() {
        List<Product> products = List.of(
                product(1, "ThinkPad", "laptop", "180000"),
                product(2, "MacBook", "laptop", "220000"),
                product(3, "IdeaPad", "laptop", "220000"),
                product(4, "MX Master", "mouse", "12000"),
                product(5, "Magic Mouse", "mouse", "12000")
        );

        Map<String, Product> result = StreamTasks.mostExpensiveProductByCategory(products);

        assertEquals("IdeaPad", result.get("laptop").name());
        assertEquals("MX Master", result.get("mouse").name());
    }

    @Test
    void lastTransactionByAccountShouldPickLatestCreatedAtAndThenGreatestId() {
        LocalDateTime ten = LocalDateTime.of(2026, 5, 1, 10, 0);
        LocalDateTime eleven = LocalDateTime.of(2026, 5, 1, 11, 0);
        List<Transaction> transactions = List.of(
                transaction(1, "acc-1", "1000", ten),
                transaction(2, "acc-1", "2000", eleven),
                transaction(3, "acc-1", "3000", eleven),
                transaction(4, "acc-2", "500", ten)
        );

        Map<String, Transaction> result = StreamTasks.lastTransactionByAccount(transactions);

        assertEquals(3, result.get("acc-1").id());
        assertEquals(4, result.get("acc-2").id());
    }

    @Test
    void countLogLevelsShouldIgnoreInvalidLinesAndUnknownLevels() {
        List<String> lines = List.of(
                "2026-05-01T10:15:00 INFO Application started",
                "2026-05-01T10:16:00 WARN Slow query",
                "2026-05-01T10:17:00 ERROR Timeout",
                "2026-05-01T10:18:00 INFO Health check",
                "broken line",
                "2026-05-01T10:19:00 TRACE Too detailed",
                "2026-05-01T10:20:00 DEBUG Cache hit"
        );

        Map<LogLevel, Long> result = StreamTasks.countLogLevels(lines);

        assertEquals(2L, result.get(LogLevel.INFO));
        assertEquals(1L, result.get(LogLevel.WARN));
        assertEquals(1L, result.get(LogLevel.ERROR));
        assertEquals(1L, result.get(LogLevel.DEBUG));
    }

    @Test
    void topTagsShouldNormalizeFlattenAndSortByFrequencyDescThenAlphabetically() {
        List<Article> articles = List.of(
                new Article(1, "Streams", List.of("Java", " Stream ", "interview")),
                new Article(2, "Collectors", List.of("java", "COLLECTORS", "stream")),
                new Article(3, "Optional", List.of("optional", "java", " ", "collectors"))
        );

        List<String> result = StreamTasks.topTags(articles, 3);

        assertEquals(List.of("java", "collectors", "stream"), result);
    }

    private static Employee employee(long id, String name, String department, String salary, boolean active) {
        return new Employee(id, name, department, new BigDecimal(salary), active);
    }

    private static Order order(long id, String customerId, OrderItem... items) {
        return new Order(id, customerId, List.of(items));
    }

    private static OrderItem item(String productName, String price, int quantity) {
        return new OrderItem(productName, new BigDecimal(price), quantity);
    }

    private static Product product(long id, String name, String category, String price) {
        return new Product(id, name, category, new BigDecimal(price));
    }

    private static Transaction transaction(long id, String accountId, String amount, LocalDateTime createdAt) {
        return new Transaction(id, accountId, new BigDecimal(amount), createdAt);
    }
}
