package ru.javabegin.micro.planner.todo.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javabegin.micro.planner.entity.Category;
import ru.javabegin.micro.planner.todo.repository.CategoryRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository=repository;
    }

    public Category findById(Long id) {
        return repository.findById(id).get();
    }

    public List<Category> findAll(UUID userId) {
        return repository.findByUserIdOrderByTitleAsc(userId);
    }

    public Category add(Category category) {
        return repository.save(category);
    }

    public Category update(Category category) {
        return repository.save(category);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<Category> findByTitle(String text, UUID userId) {
        return repository.findByTitle(text, userId);
    }
}
