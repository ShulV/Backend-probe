package ru.javabegin.micro.planner.todo.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class CategorySearchValues {

    private String title;
    private UUID userId;
}
