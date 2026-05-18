package ru.shulpov.streamapi;

import java.util.List;

public record Order(long id, String customerId, List<OrderItem> items) {
}
