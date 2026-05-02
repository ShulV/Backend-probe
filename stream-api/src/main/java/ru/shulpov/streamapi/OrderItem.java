package ru.shulpov.streamapi;

import java.math.BigDecimal;

public record OrderItem(String productName, BigDecimal price, int quantity) {
}
