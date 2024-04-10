package ru.zyablov.task.list.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import ru.zyablov.task.list.dto.TaskData;
import ru.zyablov.task.list.exception.NoSuchTaskException;
import ru.zyablov.task.list.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    TaskService taskService;
    @InjectMocks
    TaskController controller;

    @Test
    void getListTasks_TasksExist_ReturnTasks() {
        // given
        var tasks = List.of(
                new TaskData(1L, "Задача 1", "Описание 1", LocalDateTime.MAX, true),
                new TaskData(2L, "Задача 2", "Описание 2", LocalDateTime.MAX, true),
                new TaskData(3L, "Задача 3", "Описание 3", LocalDateTime.MAX, true)
        );
        doReturn(tasks).when(taskService).findAll();
        // when
        var res = controller.getListTasks();

        // then
        assertEquals(HttpStatusCode.valueOf(200), res.getStatusCode());
        assertEquals(tasks, res.getBody());
        verify(taskService).findAll();
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void getListTasks_TasksNotExist_ReturnEmptyList() {
        // given
        var tasks = List.of();
        doReturn(tasks).when(taskService).findAll();
        // when
        var res = controller.getListTasks();

        // then
        assertEquals(HttpStatusCode.valueOf(200), res.getStatusCode());
        assertEquals(tasks, res.getBody());
        verify(taskService).findAll();
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void getTask_TaskExist_ReturnTask() throws NoSuchTaskException {
        // given
        var task = new TaskData(1L, "Задача 1", "Описание 1", LocalDateTime.MAX, true);
        doReturn(task).when(taskService).findById(task.getId());

        // when
        var res = controller.getTask(task.getId());

        //then
        assertEquals(HttpStatusCode.valueOf(200), res.getStatusCode());
        assertEquals(task, res.getBody());
        verify(taskService).findById(task.getId());
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void getTask_TaskNotExist_ReturnErrorNoSuchTaskException() throws NoSuchTaskException {
        // given
        var id = 1L;
        var message = "Не найден задача с id = " + id;
        doThrow(new NoSuchTaskException(message)).when(taskService).findById(id);

        // when
        var res = controller.getTask(id);

        //then
        assertEquals(HttpStatusCode.valueOf(400), res.getStatusCode());
        assertEquals(message, res.getBody());
        verify(taskService).findById(id);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void createTask_ReqValid_ReturnTask() {
        // given
        var task = new TaskData(1L, "Задача 1", "Описание 1", LocalDateTime.MAX, true);
        doReturn(task).when(taskService).create(task);

        // when
        var res = controller.createTask(task);

        // then
        assertEquals(HttpStatusCode.valueOf(200), res.getStatusCode());
        assertEquals(task, res.getBody());
        verify(taskService).create(task);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void updateTask_ReqIsValid_ReturnTask() throws NoSuchTaskException {
        // given
        var task = new TaskData(1L, "Задача 1", "Описание 1", LocalDateTime.MAX, true);
        doReturn(task).when(taskService).update(task.getId(), task);

        // when
        var res = controller.updateTask(task.getId(), task);

        //then
        assertEquals(HttpStatusCode.valueOf(200), res.getStatusCode());
        assertEquals(task, res.getBody());
        verify(taskService).update(task.getId(), task);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void updateTask_ReqIsInvalid_ReturnErrorNoSuchTaskException() throws NoSuchTaskException {
        // given
        var task = new TaskData(1L, "Задача 1", "Описание 1", LocalDateTime.MAX, true);
        var message = "Не найден задача с id = " + task.getId();
        doThrow(new NoSuchTaskException(message)).when(taskService).update(task.getId(), task);

        // when
        var res = controller.updateTask(task.getId(), task);

        //then
        assertEquals(HttpStatusCode.valueOf(400), res.getStatusCode());
        assertEquals(message, res.getBody());
        verify(taskService).update(task.getId(), task);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void deleteTask_TaskExistOrNotExist_ReturnsNoContent() {
        // given
        var id = 1L;
        doNothing().when(taskService).delete(id);
        //when
        var res = controller.deleteTask(id);

        //then
        assertEquals(HttpStatusCode.valueOf(204), res.getStatusCode());
        assertNull(res.getBody());
        verify(taskService).delete(id);
        verifyNoMoreInteractions(taskService);
    }
}