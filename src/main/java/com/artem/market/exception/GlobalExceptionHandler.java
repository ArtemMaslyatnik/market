package com.artem.market.exception; // Указываем правильный пакет

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Говорит Spring, что этот класс перехватывает ошибки со ВСЕХ контроллеров
public class GlobalExceptionHandler {

    // Перехватываем наше кастомное бизнес-исключение
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Object> handleBusinessRuleException(BusinessRuleException ex) {

        // Канонично возвращать не просто строку, а красивый JSON с деталями ошибки
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Business Rule Violation");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST); // Возвращаем 400 Bad Request
    }
}