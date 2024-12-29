package com.vladima.gamingrental.handlers;

import com.vladima.gamingrental.exceptions.EntityOperationException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
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

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler({ RequestNotPermitted.class })
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public void handleRequestNotPermitted() {}

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
