package com.example.todolist.repositories;

import com.example.todolist.models.Task;
import com.mongodb.client.result.DeleteResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface CustomRepository {
    //save
    //deleteById
    //findAll(pageRequest)
    //findById(id)
    //findByStatus(status, pageRequest)
    //existsByTitle(taskTitle)
    Task save(Task task);
    DeleteResult deleteById(String id);
    Optional<Task> findById(String id);
    Page<Task> findByStatus(String status, PageRequest pageRequest);
    Page<Task> findAll(PageRequest pageRequest);
    boolean existsByTitle(String title);
}
