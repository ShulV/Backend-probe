package ru.shulpov.streamapi;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    /**
     * Найти топ N самых частых слов. Сортировка: частота по убыванию, затем слово по алфавиту.
     */
    public static List<String> topNFrequentWords(List<String> words, int limit) {
        return words.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted((c1, c2) -> {
                    if (c1.getValue() < c2.getValue()) {
                        return 1;
                    } else if (c1.getValue() > c2.getValue()) {
                        return -1;
                    } else {
                        return c1.getKey().compareTo(c2.getKey());
                    }
                })
                .map(Map.Entry::getKey)
                .limit(limit)
                .toList();
    }

    /**
     * Сгруппировать активных сотрудников по отделу.
     * Внутри отдела имена должны быть отсортированы по зарплате по убыванию, затем по имени.
     */
    public static Map<String, List<String>> activeEmployeeNamesByDepartment(List<Employee> employees) {
        return employees.stream()
                .filter(Employee::active)
                .collect(Collectors.groupingBy(
                        Employee::department,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Employee::salary, Comparator.reverseOrder())
                                                .thenComparing(Employee::name))
                                        .map(Employee::name)
                                        .toList()
                        )));

    }

    /**
     * Посчитать среднюю зарплату активных сотрудников по каждому отделу
     */
    public static Map<String, BigDecimal> averageSalaryByDepartment(List<Employee> employees) {
        return employees.stream()
                .filter(Employee::active)
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    BigDecimal salarySum = list.stream()
                                            .map(Employee::salary)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                                    BigDecimal employeeSize = new BigDecimal(list.size());
                                    return salarySum.divide(employeeSize, RoundingMode.HALF_EVEN);
                                }
                )));
    }

    /**
     * Найти сотрудника со второй уникальной по величине зарплатой
     */
    public static Optional<Employee> secondHighestPaidEmployee(List<Employee> employees) {
        return employees.stream()
                .map(Employee::salary)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .limit(1)
                .flatMap(secondSalary -> employees.stream()
                        .filter(employee -> secondSalary.equals(employee.salary())))
                .findFirst();
    }

    /**
     * Посчитать общую сумму заказов по каждому клиенту с учетом цены и количества товаров.
     */
    public static Map<String, BigDecimal> totalAmountByCustomer(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        Order::customerId,
                        Collectors.mapping(
                                o -> o.items().stream()
                                        .map(oi -> oi.price().multiply(new BigDecimal(oi.quantity())))
                                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)

                        )));

    }

    /**
     * Найти самый дорогой товар в каждой категории.
     * При равной цене выбрать товар с лексикографически меньшим названием.
     */
    public static Map<String, Product> mostExpensiveProductByCategory(List<Product> products) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::category,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Product::price, Comparator.reverseOrder())
                                                .thenComparing(Product::name))
                                        .limit(1)
                                        .findFirst().orElse(null)
                        )));
    }

    /**
     * Для каждого аккаунта найти последнюю транзакцию по времени.
     * При одинаковом времени выбрать транзакцию с большим `id`.
     */
    public static Map<String, Transaction> lastTransactionByAccount(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::accountId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .min(Comparator.comparing(Transaction::createdAt, Comparator.reverseOrder())
                                        .thenComparing(Transaction::id, Comparator.reverseOrder())).orElse(null)
                        ))
                );
    }

    /**
     * Распарсить строки логов и посчитать уровни `INFO`, `WARN`, `ERROR`, `DEBUG`. Некорректные строки игнорировать.
     */
    public static Map<LogLevel, Long> countLogLevels(List<String> lines) {
        throw new UnsupportedOperationException("Task 9 is not implemented yet");
    }

    /**
     * Собрать теги из статей, нормализовать их, удалить пустые и вернуть топ N по частоте.
     * Сортировка: частота по убыванию, затем тег по алфавиту.
     */
    public static List<String> topTags(List<Article> articles, int limit) {
        throw new UnsupportedOperationException("Task 10 is not implemented yet");
    }
}
