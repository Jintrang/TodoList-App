package com.example.todolist.repositories;

import com.example.todolist.models.Task;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    public Optional<Task> findById(String id);
    public Task findByTitle(String title);
    Page<Task> findAll(Pageable pageable);
    List<Task> findByStatus(String status);
    //public Optional<Task> save(Task task);
    public void deleteById(String id);
}
