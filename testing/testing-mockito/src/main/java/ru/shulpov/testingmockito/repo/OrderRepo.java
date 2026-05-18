package ru.shulpov.testingmockito.repo;

import ru.shulpov.testingmockito.model.Order;

import java.util.Optional;

public interface OrderRepo {
    Order save(Order order);
    Optional<Order> findById(Long id);
}

