package ru.zyablov.task.list.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zyablov.task.list.dto.TaskData;
import ru.zyablov.task.list.exception.NoSuchTaskException;
import ru.zyablov.task.list.service.TaskService;

import java.util.List;

/**
 * Контроллер для работы с задачами.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private static final String APPLICATION_JSON = "application/json";

    /**
     * Получить все задачи.
     *
     * @return List<String>
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping(value = "", produces = APPLICATION_JSON)
    public ResponseEntity<List<TaskData>> getListTasks() {
        return ResponseEntity.ok(taskService.findAll());
    }

    /**
     * Получить задачу по id.
     *
     * @return String
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON)
    public ResponseEntity<?> getTask(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(taskService.findById(id));
        } catch (NoSuchTaskException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Создать задачу.
     *
     * @return String
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PostMapping(value = "", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public ResponseEntity<TaskData> createTask(@RequestBody TaskData taskData) {
        return ResponseEntity.ok(taskService.create(taskData));
    }

    /**
     * Обновить задачу.
     *
     * @return String
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public ResponseEntity<?> updateTask(@PathVariable("id") Long id, @RequestBody TaskData taskData) {
        try {
            return ResponseEntity.ok(taskService.update(id, taskData));
        } catch (NoSuchTaskException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Удалить задачу.
     */
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Успешный ответ"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON)
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
