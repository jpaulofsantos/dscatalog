package com.developer.dscatalog.resources.exceptions;

import com.developer.dscatalog.services.exceptions.DatabaseException;
import com.developer.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class) //indica a exception que será direcioanda para o handler
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(status.value());
        standardError.setError("Resource not found");
        standardError.setMessage(e.getMessage());
        standardError.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(DatabaseException.class) //indica a exception que será direcioanda para o handler
    public ResponseEntity<StandardError> dataBase(DatabaseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(status.value());
        standardError.setError("Database exception");
        standardError.setMessage(e.getMessage());
        standardError.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }
}
