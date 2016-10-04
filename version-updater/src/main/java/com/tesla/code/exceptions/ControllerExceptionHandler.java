package com.tesla.code.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ControllerExceptionHandler {

    public ControllerExceptionHandler() {

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UniquenessException.class)
    public String handleUniquenessException(UniquenessException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingDataException.class)
    public String handleMissingDataException(MissingDataException e) {
        return e.getMessage();
    }
}
