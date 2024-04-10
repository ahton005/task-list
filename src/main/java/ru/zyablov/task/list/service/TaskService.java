package ru.zyablov.task.list.service;

import ru.zyablov.task.list.dto.TaskData;
import ru.zyablov.task.list.exception.NoSuchTaskException;

import java.util.List;

public interface TaskService {
    /**
     * Получить все задачи.
     *
     * @return List<TaskData>
     */
    List<TaskData> findAll();

    /**
     * Получить задачу по id.
     *
     * @return TaskData
     */
    TaskData findById(Long id) throws NoSuchTaskException;

    /**
     * Создать задачу.
     *
     * @return TaskData
     */
    TaskData create(TaskData taskData);

    /**
     * Обновить задачу.
     *
     * @return TaskData
     */
    TaskData update(Long id, TaskData taskData) throws NoSuchTaskException;

    /**
     * Удалить задачу.
     */
    void delete(Long id);
}
