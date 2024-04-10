package ru.zyablov.task.list.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zyablov.task.list.dto.TaskData;
import ru.zyablov.task.list.exception.NoSuchTaskException;
import ru.zyablov.task.list.repository.TaskRepository;
import ru.zyablov.task.list.utils.ConverterUtils;

import java.util.List;
import java.util.stream.Collectors;

import static ru.zyablov.task.list.utils.ConverterUtils.apiToDomain;
import static ru.zyablov.task.list.utils.ConverterUtils.domainToApi;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;


    @Override
    public List<TaskData> findAll() {
        return taskRepository.findAll().stream().map(ConverterUtils::domainToApi).collect(Collectors.toList());
    }

    @Override
    public TaskData findById(Long id) throws NoSuchTaskException {
        return domainToApi(taskRepository.findById(id).orElseThrow(() -> new NoSuchTaskException("Не найден задача с id = " + id)));
    }

    @Override
    @Transactional
    public TaskData create(TaskData taskData) {
        var newTask = taskRepository.save(apiToDomain(taskData));
        return domainToApi(newTask);
    }

    @Override
    @Transactional
    public TaskData update(Long id, TaskData taskData) throws NoSuchTaskException {
        taskRepository.findById(id).orElseThrow(() -> new NoSuchTaskException("Не найден задача с id = " + id));
        var newTask = apiToDomain(taskData);
        newTask.setId(id);
        var resultTask = taskRepository.save(newTask);
        return domainToApi(resultTask);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
