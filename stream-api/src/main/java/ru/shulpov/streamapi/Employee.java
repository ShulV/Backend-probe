package ru.shulpov.streamapi;

import java.math.BigDecimal;

public record Employee(long id, String name, String department, BigDecimal salary, boolean active) {
}
