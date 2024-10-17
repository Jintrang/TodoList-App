package com.example.todolist.controllers;

import com.example.todolist.dtos.TaskDTO;
import com.example.todolist.models.Task;
import com.example.todolist.repositories.TaskRepository;
import com.example.todolist.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("")
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskDTO taskDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorsMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage).collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorsMessage);
            }
            Task newTask = taskService.createTask(taskDTO);
            return ResponseEntity.ok().body(newTask);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @GetMapping("")
//    public ResponseEntity<?> getAllTasks(@RequestParam ("page") int page, @RequestParam("limit") int limit ) {
//        return ResponseEntity.ok().body(page +" : " + limit);
//    }
    @GetMapping("/arrangement")
    public ResponseEntity<?> getTaskByArrangement(@RequestParam("page") int page,
                                                  @RequestParam("limit") int limit,
                                                  @RequestParam("arrangement") String arrangement ) {
        PageRequest pageRequest;
        switch (arrangement.toUpperCase()) {
            case "":
            case "LASTDATE":
                pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
                break;
            case "MINSCORE":
                pageRequest = PageRequest.of(page, limit, Sort.by("score").descending());
                break;
            case "MAXSCORE":
                pageRequest = PageRequest.of(page, limit, Sort.by("score").ascending());
                break;
            case "FISRTDATE":
                pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").ascending());
                break;
            default:
                return ResponseEntity.badRequest().body("Cannot sort by" + arrangement);
        }
        Page<Task> tasks = taskService.getAllTasks(pageRequest);
        List<Task> taskList = tasks.getContent();
        return ResponseEntity.ok().body(taskList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable String id) {
        try {
            Task existingTask = taskService.getTaskById(id);
            return ResponseEntity.ok().body(existingTask);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getTaskByStatus(@RequestParam("status") String status) {
        try {
            List<Task> taskList = taskService.getTaskByStatus(status.toUpperCase());
            return ResponseEntity.ok().body(taskList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@Valid @RequestBody TaskDTO taskDTO, @PathVariable String id, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorsMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage).collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorsMessage);
            }
            Task existingTask = taskService.updateTask(id, taskDTO);
            return ResponseEntity.ok().body(existingTask);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok().body("Delete task with id = " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
