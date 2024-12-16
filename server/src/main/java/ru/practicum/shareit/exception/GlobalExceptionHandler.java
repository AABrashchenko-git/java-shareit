package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put("error", error.getDefaultMessage())
        );
        log.warn("Validation failure: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundExceptions(NotFoundException ex) {
        log.warn("Processing failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[]");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access is denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("[]");
    }

    @ExceptionHandler(DuplicatedDataException.class)
    public ResponseEntity<String> handleDuplicatedDataException(DuplicatedDataException ex) {
        log.warn("Processing failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body("[]");
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        log.warn("Processing failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("[]");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleInternalServerError(Throwable th) {
        log.warn("Processing failed: {}", th.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("[]");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("Processing failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid JSON format"));
    }
}
