package ru.shulpov.streamapi;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(long id, String accountId, BigDecimal amount, LocalDateTime createdAt) {
}
