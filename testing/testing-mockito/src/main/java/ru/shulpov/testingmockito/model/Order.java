package ru.shulpov.testingmockito.model;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private Long id;
    private List<String> items;
    private BigDecimal total;

    public Order(Long id, List<String> items, BigDecimal total) {
        this.id = id;
        this.items = items;
        this.total = total;
    }

    public Long getId() { return id; }
    public List<String> getItems() { return items; }
    public BigDecimal getTotal() { return total; }
}

