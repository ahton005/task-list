package ru.zyablov.task.list.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Объект транспорта.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskData {
    private long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private boolean completed;
}
