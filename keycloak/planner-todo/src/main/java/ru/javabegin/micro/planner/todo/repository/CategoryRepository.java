package ru.javabegin.micro.planner.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javabegin.micro.planner.entity.Category;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

//    // поиск категорий пользователя (по названию)
//    List<Category> findByUserEmailOrderByTitleAsc(String email);

    // поиск категорий пользователя (по названию)
    List<Category> findByUserIdOrderByTitleAsc(UUID userId);

    @Query("select c FROM Category c where " +
            "(:title is null or :title='' " +
            "or lower(c.title) like lower(concat('%', :title, '%'))) " +
            "and c.userId=:userId " +
            "order by c.title asc")
    List<Category> findByTitle(@Param("title") String title, @Param("userId") UUID userId);
}
