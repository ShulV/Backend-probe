package ru.shulpov.testingmockito.service;

import org.springframework.stereotype.Service;
import ru.shulpov.testingmockito.model.Order;
import ru.shulpov.testingmockito.repo.OrderRepo;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final OrderRepo repo;

    public OrderService(OrderRepo repo) {
        this.repo = repo;
    }

    public Order create(Order order) {
        if (order.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total must be > 0");
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Items must not be empty");
        }
        return repo.save(order);
    }

    public Order getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
    }
}

