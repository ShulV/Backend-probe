package ru.shulpov.streamapi;

import java.math.BigDecimal;

public record Product(long id, String name, String category, BigDecimal price) {
}
