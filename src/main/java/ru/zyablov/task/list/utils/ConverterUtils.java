package ru.zyablov.task.list.utils;

import ru.zyablov.task.list.dto.TaskData;
import ru.zyablov.task.list.entity.Task;

public class ConverterUtils {
    private ConverterUtils() {
    }

    /**
     * Преобразовать задачу в объект транспорта.
     *
     * @return TaskData
     */
    public static TaskData domainToApi(Task task) {
        if (task == null) return TaskData.builder().build();
        return TaskData.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .completed(task.isCompleted())
                .build();
    }

    /**
     * Преобразовать задачу в объект БД.
     *
     * @return Task
     */
    public static Task apiToDomain(TaskData taskData) {
        if (taskData == null) return Task.builder().build();
        return Task.builder()
                .title(taskData.getTitle())
                .description(taskData.getDescription())
                .dueDate(taskData.getDueDate())
                .completed(taskData.isCompleted())
                .build();
    }
}
