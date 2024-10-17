package com.example.todolist.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {
    @NotEmpty(message = "Title cannot be null")
    @Size(min = 1, max = 100, message = "The title must be >=1 and <=100")
    private String title;

    private String description;

    @NotEmpty(message = "StatusId cannot be null")
    private String status;

    @NotNull(message = "Score cannot be null")
    @Min(value = 0, message = "Score must be >= 0")
    private int score;

    @Future(message = "Deadline must be >= now")
    private LocalDate deadline;
}
