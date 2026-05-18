package ru.shulpov.streamapi;

import java.util.Arrays;
import java.util.List;

public enum LogLevel {
    INFO,
    WARN,
    ERROR,
    DEBUG;

    final static List<String> ALL = Arrays.stream(values()).map(LogLevel::toString).toList();
}
