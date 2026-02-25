package com.kayky.backend.web;

import com.kayky.backend.service.exception.BadRequestException;
import com.kayky.backend.service.exception.ConflictException;
import com.kayky.backend.service.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex, Locale locale, HttpServletRequest req) {
        String message = messageSource.getMessage(ex.getMessageKey(), null, locale);
        return buildError(HttpStatus.NOT_FOUND, message, req, null);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflict(ConflictException ex, Locale locale, HttpServletRequest req) {
        String message = messageSource.getMessage(ex.getMessageKey(), null, locale);
        return buildError(HttpStatus.CONFLICT, message, req, null);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex, Locale locale, HttpServletRequest req) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), locale);
        return buildError(HttpStatus.BAD_REQUEST, message, req, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, Locale locale, HttpServletRequest req) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            // pega a mensagem resolvida certinho (locale + messages.properties)
            String msg = messageSource.getMessage(fe, locale);
            fieldErrors.put(fe.getField(), msg);
        }

        String message = messageSource.getMessage("error.validation", null, locale);
        return buildError(HttpStatus.BAD_REQUEST, message, req, fieldErrors);
    }

    // fallback: se estourar qualquer outra coisa, você não fica “no escuro”
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex, HttpServletRequest req) {
        // em produção você pode logar ex aqui
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req, null);
    }

    private ResponseEntity<?> buildError(HttpStatus status, String message, HttpServletRequest req, Map<String, String> fieldErrors) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", message);
        body.put("path", req.getRequestURI());
        body.put("method", req.getMethod());

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            body.put("fieldErrors", fieldErrors);
        }

        return ResponseEntity.status(status).body(body);
    }
}