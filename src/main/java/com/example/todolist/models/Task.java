package com.example.todolist.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection="tasks")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task extends BaseEntity{
    public static String COMPLETED = "COMPLETED";
    public static String PROCESSING = "PROCESSING";
    public static String UNSTARTED = "UNSTARTED";
    @Id
    private String id;

    private String title;

    private String description;

    private int score;

    private String status;

    private LocalDate deadline;

}
