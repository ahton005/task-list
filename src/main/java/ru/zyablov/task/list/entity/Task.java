package ru.zyablov.task.list.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Сущность для хранения задач в БД.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime lastUpd;
    @NotNull(message = "Поле title не должно быть пустым")
    @Size(min = 3, max = 200, message = "Размер title должен находиться в диапазоне от 3 до 200 символов")
    private String title;
    @Size(max = 2000, message = "Размер description должен быть не более 2000 символов")
    private String description;
    @NotNull(message = "Поле dueDate не должно быть пустым")
    private LocalDateTime dueDate;
    private boolean completed;
}
