package ru.shulpov.streamapi;

import java.util.List;

public record Article(long id, String title, List<String> tags) {
}
