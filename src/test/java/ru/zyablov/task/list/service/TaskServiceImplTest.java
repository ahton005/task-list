package ru.zyablov.task.list.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zyablov.task.list.dto.TaskData;
import ru.zyablov.task.list.entity.Task;
import ru.zyablov.task.list.exception.NoSuchTaskException;
import ru.zyablov.task.list.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    TaskRepository taskRepository;
    @InjectMocks
    TaskServiceImpl taskService;

    @Test
    void findAll_TasksExist_ReturnTasks() {
        //given
        LocalDateTime now = LocalDateTime.now();
        var tasksData = List.of(
                new TaskData(1L, "Задача 1", "Описание 1", now.minusHours(2), true),
                new TaskData(2L, "Задача 2", "Описание 2", now.plusHours(24), true),
                new TaskData(3L, "Задача 3", "Описание 3", LocalDateTime.MAX, false),
                new TaskData()
        );

        var tasks = List.of(
                new Task(1L, now, now, "Задача 1", "Описание 1", now.minusHours(2), true),
                new Task(2L, now, now, "Задача 2", "Описание 2", now.plusHours(24), true),
                new Task(3L, now, now, "Задача 3", "Описание 3", LocalDateTime.MAX, false),
                new Task(0, null, null, null, null, null, false)
        );

        doReturn(tasks).when(taskRepository).findAll();

        // when
        var res = taskService.findAll();

        // then
        assertEquals(tasksData, res);
        verify(taskRepository).findAll();
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void findAll_TasksNotExist_ReturnEmptyList() {
        //given
        var tasks = List.of();

        doReturn(tasks).when(taskRepository).findAll();

        // when
        var res = taskService.findAll();

        // then
        assertEquals(tasks, res);
        verify(taskRepository).findAll();
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void findAll_TasksExist_ValuesIsNotValid_ReturnEmptyTask() {
        //given
        var tasksData = List.of(
                new TaskData()
        );

        var tasks = List.of(
                new Task(0, null, null, null, null, null, false)
        );

        doReturn(tasks).when(taskRepository).findAll();

        // when
        var res = taskService.findAll();

        // then
        assertEquals(tasksData, res);
        verify(taskRepository).findAll();
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void findAll_TasksExist_ValuesContainsNull_ReturnEmptyTask() {
        //given
        var tasks = new ArrayList<>();
        tasks.add(null);
        var tasksData = List.of(new TaskData());

        doReturn(tasks).when(taskRepository).findAll();

        // when
        var res = taskService.findAll();

        // then
        assertEquals(tasksData, res);
        verify(taskRepository).findAll();
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void findById_TaskExist_ReturnTask() throws NoSuchTaskException {
        //given
        LocalDateTime now = LocalDateTime.now();
        var id = 1L;
        var task = new Task(1L, now, now, "Задача 1", "Описание 1", now.minusHours(2), true);
        var tasksData = new TaskData(1L, "Задача 1", "Описание 1", now.minusHours(2), true);

        doReturn(Optional.of(task)).when(taskRepository).findById(id);

        // when
        var res = taskService.findById(id);

        // then
        assertEquals(tasksData, res);
        verify(taskRepository).findById(id);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void findById_TaskNotExist_ReturnNoSuchTaskException() {
        //given
        var id = 1L;
        var message = "Не найден задача с id = " + id;

        doReturn(Optional.empty()).when(taskRepository).findById(id);

        // when
        var e = assertThrows(NoSuchTaskException.class, () -> taskService.findById(id));

        // then
        assertEquals(message, e.getMessage());
        verify(taskRepository).findById(id);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void findById_TaskIsEmpty_ReturnEmptyTask() throws NoSuchTaskException {
        //given
        var id = 1L;
        var task = new Task();
        var tasksData = new TaskData(0, null, null, null, false);

        doReturn(Optional.of(task)).when(taskRepository).findById(id);

        // when
        var res = taskService.findById(id);

        // then
        assertEquals(tasksData, res);
        verify(taskRepository).findById(id);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void create_TaskIsValid_ReturnCreatedTask() {
        //given
        LocalDateTime now = LocalDateTime.now();
        var task = new Task(0L, null, null, "Задача 1", "Описание 1", now.minusHours(2), true);
        var taskMock = new Task(1L, now, now, "Задача 1", "Описание 1", now.minusHours(2), true);
        var tasksData = new TaskData(1L, "Задача 1", "Описание 1", now.minusHours(2), true);
        doReturn(taskMock).when(taskRepository).save(task);

        // when
        var res = taskService.create(tasksData);

        // then
        assertEquals(tasksData, res);
        verify(taskRepository).save(task);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void create_TaskIsInvalid_ReturnEmptyTask() {
        //given
        var task = new Task(0, null, null, null, null, null, false);
        var tasksData = new TaskData(0, null, null, null, false);
        doReturn(task).when(taskRepository).save(task);

        // when
        var res = taskService.create(tasksData);

        // then
        assertEquals(tasksData, res);
        verify(taskRepository).save(task);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void update_TaskExist_ReturnUpdatedTask() throws NoSuchTaskException {
        // given
        LocalDateTime now = LocalDateTime.now();
        var id = 1L;
        var task = new Task(1L, null, null, "Задача 1", "Описание 1", now.minusHours(2), true);
        var taskMock = new Task(1L, now, now, "Задача 1", "Описание 1", now.minusHours(2), true);
        var tasksData = new TaskData(1L, "Задача 1", "Описание 1", now.minusHours(2), true);

        doReturn(taskMock).when(taskRepository).save(task);
        doReturn(Optional.of(taskMock)).when(taskRepository).findById(id);

        //when
        var res = taskService.update(id, tasksData);

        //then
        assertEquals(tasksData, res);
        verify(taskRepository).save(task);
        verify(taskRepository).findById(id);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void update_TaskNotExist_ReturnNoSuchTaskException() {
        // given
        LocalDateTime now = LocalDateTime.now();
        var id = 1L;
        var message = "Не найден задача с id = " + id;
        var tasksData = new TaskData(1L, "Задача 1", "Описание 1", now.minusHours(2), true);

        doReturn(Optional.empty()).when(taskRepository).findById(id);

        //when
        var e = assertThrows(NoSuchTaskException.class, () -> taskService.update(id, tasksData));

        //then
        assertEquals(message, e.getMessage());
        verify(taskRepository).findById(id);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void delete_ReturnVoid() {
        //given
        var id = 1L;
        doNothing().when(taskRepository).deleteById(id);

        // when
        taskService.delete(id);

        // then
        verify(taskRepository).deleteById(id);
    }
}