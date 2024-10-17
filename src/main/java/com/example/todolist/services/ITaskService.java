package com.example.todolist.services;

import com.example.todolist.dtos.TaskDTO;
import com.example.todolist.exceptions.DataNotFoundException;
import com.example.todolist.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITaskService {
    Task createTask(TaskDTO taskDTO) throws Exception;
    Task updateTask(String id, TaskDTO taskDTO) throws Exception;
    void deleteTask(String id) throws Exception;
    Page<Task> getAllTasks(PageRequest pageRequest);
    Task getTaskById(String id) throws Exception;
    List<Task> getTaskByStatus(String statusId) throws Exception;
}
