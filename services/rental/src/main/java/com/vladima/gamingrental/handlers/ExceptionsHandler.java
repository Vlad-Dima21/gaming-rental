package com.vladima.gamingrental.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladima.gamingrental.exceptions.EntityOperationException;
import feign.FeignException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@RestControllerAdvice
public class ExceptionsHandler {

    @Data
    @AllArgsConstructor
    public static class ExceptionFormat {
        String message, details, fieldName;
    }

    @ExceptionHandler({FeignException.class})
    public ResponseEntity<Map<String, String>> feignException(FeignException e) {
        Map<String, String> json = new HashMap<>();
        try {
            json = new ObjectMapper().readValue(e.contentUTF8(), new TypeReference<>() {
            });
            return new ResponseEntity<>(json, HttpStatus.valueOf(e.status()));
        } catch (JsonProcessingException ex) {
            return new ResponseEntity<>(json, HttpStatus.valueOf(e.status()));
        }
    }

    @ExceptionHandler({ RequestNotPermitted.class })
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public void handleRequestNotPermitted() {}

    @ExceptionHandler({TimeoutException.class})
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public void handleTimeoutException() {
    }

    @ExceptionHandler({EntityOperationException.class})
    public ResponseEntity<Map<String, String>> operationException(EntityOperationException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        if (!e.getExtraInfo().isBlank()) response.put("details", e.getExtraInfo());
        if (e.getFieldName() != null && !e.getFieldName().isBlank()) response.put("fieldName", e.getFieldName());
        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> operationException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        String fieldName = e.getBindingResult().getFieldError().getField();
        String fieldMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        response.put("details", MessageFormat.format("Invalid value for {0}", getReadableName(fieldName)));
        response.put("message", fieldMessage);
        response.put("fieldName", fieldName);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, String>> operationException(MethodArgumentTypeMismatchException e) {
        Map<String, String> response = new HashMap<>();
        String fieldName = e.getPropertyName();
        String fieldMessage = MessageFormat.format("Invalid {0}", fieldName);
        response.put("details", MessageFormat.format("Invalid value for {0}", getReadableName(fieldName)));
        response.put("message", fieldMessage);
        response.put("fieldName", fieldName);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private String getReadableName(String field) {
        return StringUtils.join(
                Arrays.stream(StringUtils.splitByCharacterTypeCamelCase(field)).map(String::toLowerCase).toArray(),
                ' '
        );
    }
}
