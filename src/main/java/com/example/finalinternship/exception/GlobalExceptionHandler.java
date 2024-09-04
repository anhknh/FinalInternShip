package com.example.finalinternship.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = messageSource.getMessage(error.getCode(), error.getArguments(), error.getDefaultMessage(), LocaleContextHolder.getLocale());
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(ResourceNotFoundException ex) {
        Locale locale = LocaleContextHolder.getLocale();

        String errorMessage = messageSource.getMessage("error.notfound", new Object[]{ex.getResourceName(), ex.getResourceId()}, locale);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DuplicateCourseCodeException.class})
    public ResponseEntity<String> handleDuplicateException(DuplicateCourseCodeException ex) {
        Locale locale = LocaleContextHolder.getLocale();

        String errorMessage = messageSource.getMessage("error.duplicate", new Object[]{ex.getResourceName(), ex.getResourceCode()}, locale);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String errorMessage = messageSource.getMessage("error.notmatch", new Object[]{ex.getName(),ex.getName()}, locale);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }


}
