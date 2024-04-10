package ru.zyablov.task.list.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zyablov.task.list.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
