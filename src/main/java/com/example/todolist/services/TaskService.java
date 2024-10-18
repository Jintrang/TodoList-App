package com.example.todolist.services;

import com.example.todolist.dtos.TaskDTO;
import com.example.todolist.exceptions.DataNotFoundException;
import com.example.todolist.models.Task;
import com.example.todolist.repositories.*;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {
    private final TaskCustomRepository taskRepository;
    @Override
    public Task createTask(TaskDTO taskDTO) throws Exception {
        Task newTask = Task.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .status(taskDTO.getStatus().toUpperCase())
                .deadline(taskDTO.getDeadline())
                .score(taskDTO.getScore())
                .build();
        taskRepository.save(newTask);
        return newTask;
    }

    @Override
    public Task updateTask(String id, TaskDTO taskDTO) throws Exception {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find task with id: " + id));
        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setStatus(taskDTO.getStatus().toUpperCase());
        taskRepository.save(existingTask);
        return existingTask;
    }

    @Override
    public void deleteTask(String id) throws Exception {
        Task existingTask = getTaskById(id);
        taskRepository.deleteById(id);
    }

    @Override
    public Page<Task> getAllTasks(PageRequest pageRequest) {
        return taskRepository.findAll(pageRequest);
    }

    @Override
    public Task getTaskById(String id) throws Exception {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find task with id: " + id));
        return existingTask;
    }

    @Override
    public Page<Task> getTaskByStatus(String status, PageRequest pageRequest) throws Exception{
        return taskRepository.findByStatus(status, pageRequest);
    }

    @Override
    public boolean exitsByTitle(String taskTitle) {
        return taskRepository.existsByTitle(taskTitle);
    }

    @Override
    public Page<Task> getTasks(int page, int limit, String arrangement, String status) throws Exception {
        PageRequest pageRequest = createPageRequest(page, limit, arrangement);
        if (status.equalsIgnoreCase("")) {
            return getAllTasks(pageRequest);
        } else {
                return getTaskByStatus(status.toUpperCase(), pageRequest);
        }
    }
    private PageRequest createPageRequest(int page, int limit, String arrangement) {
        return switch (arrangement.toUpperCase()) {
            case "", "LASTDATE" -> PageRequest.of(page, limit, Sort.by("createdAt").descending());
            case "MINSCORE" -> PageRequest.of(page, limit, Sort.by("score").ascending());
            case "MAXSCORE" -> PageRequest.of(page, limit, Sort.by("score").descending());
            case "FISRTDATE" -> PageRequest.of(page, limit, Sort.by("createdAt").ascending());
            default -> throw new IllegalArgumentException("Invalid arrangement");
        };
    }
}
