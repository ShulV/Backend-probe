package ru.javabegin.micro.planner.todo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.javabegin.micro.planner.entity.Stat;

import java.util.UUID;

// принцип ООП: абстракция-реализация - здесь описываем все доступные способы доступа к данным
@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {

    Stat findByUserId(UUID userId); // всегда получаем только 1 объект, т.к. 1 пользователь содержит только 1 строку статистики (связь "один к одному")
}
