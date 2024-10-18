package com.example.todolist.controllers;

import com.example.todolist.dtos.TaskDTO;
import com.example.todolist.exceptions.DataNotFoundException;
import com.example.todolist.models.Task;
import com.example.todolist.repositories.TaskRepository;
import com.example.todolist.services.TaskService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("")
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskDTO taskDTO,
                                        BindingResult result) {
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

    @GetMapping("")
    public ResponseEntity<?> getAllTask(@RequestParam("page") int page,
                                                  @RequestParam("limit") int limit,
                                                  @RequestParam("arrangement") String arrangement,
                                                  @RequestParam("status") String status,
                                                  Model model) {
        Page<Task> tasks = null;
        try {
            tasks = taskService.getTasks(page, limit, arrangement, status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<Task> taskList = tasks.getContent();
        model.addAttribute("arrange", arrangement);
        model.addAttribute("tasks", taskList);
        return ResponseEntity.ok().body(taskList);
        //return "tasks";
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

//    @GetMapping("")
//    public ResponseEntity<?> getTaskByStatus(@RequestParam("status") String status) {
//        try {
//            List<Task> taskList = taskService.getTaskByStatus(status.toUpperCase());
//            return ResponseEntity.ok().body(taskList);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

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

    @PostMapping("/generateFakeTask")
    public ResponseEntity<?> generateFakeTasks() {
        Faker faker = new Faker();
        Random random = new Random();
        String[] statuses = {"FINISHED", "PROCESSING", "UNSTARTED"};
        for (int i = 0; i < 100; i++) {
            String taskTitle = faker.book().title();
            String status = statuses[random.nextInt(statuses.length)];
            if(taskService.exitsByTitle(taskTitle)) continue;
            TaskDTO taskDTO = TaskDTO.builder()
                    .title(taskTitle)
                    .description(faker.lorem().sentence())
                    .score((int)faker.number().numberBetween(0, 100))
                    .status(status)
                    .deadline(LocalDate.now().plusDays(faker.number().numberBetween(1, 30)))
                    .build();
            try {
                Task fakeTask = taskService.createTask(taskDTO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.ok().body("Create fake Task");
    }
}
