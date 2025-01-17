package it.epicode.W6_D5_test_Gestione_Eventi2.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class HandlerExceptions extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<Object> entityNotFound(EntityNotFoundException e) {
        return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = PrenotazioneException.class)
    protected ResponseEntity<Object> prenotazioneNonPermessa(PrenotazioneException e) {
        return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EventoException.class)
    protected ResponseEntity<Object> eventoException(EventoException e) {
        return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            if (fieldName.contains(".")) {
                fieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
            }
            errors.put(fieldName, violation.getMessage());

        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}