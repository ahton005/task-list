package ru.zyablov.task.list.advice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class BadRequestControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleBindException(Exception exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
        problemDetail.setProperty("message", exception.getMessage());
        return ResponseEntity.internalServerError().body(problemDetail);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleBindException(ConstraintViolationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Ошибка валидации");
        problemDetail.setProperty("errors", exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
