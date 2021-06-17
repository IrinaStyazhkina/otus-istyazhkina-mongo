package ru.otus.istyazhkina.library.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.otus.istyazhkina.library.exception.DataOperationException;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(DataOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> exception(DataOperationException ex) {
        String errorMessage = (ex != null ? ex.getMessage() : "Unknown error");
        return ResponseEntity.badRequest().body(errorMessage);
    }
}

