package com.switchfully.eurder.controllers;

import com.switchfully.eurder.exceptions.UnauthorizedException;
import com.switchfully.eurder.exceptions.UnknownUserException;
import com.switchfully.eurder.exceptions.UserAlreadyExistsException;
import com.switchfully.eurder.exceptions.WrongPasswordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UnauthorizedException.class, WrongPasswordException.class, UserAlreadyExistsException.class})
    protected void resourceNotAuthorized(RuntimeException exception, HttpServletResponse response) throws IOException {
        log.warn(exception.getMessage());
        response.sendError(HttpServletResponse.SC_FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler({UnknownUserException.class, NullPointerException.class, NoSuchElementException.class})
    protected void resourceNotfound(RuntimeException exception, HttpServletResponse response) throws IOException {
        log.warn(exception.getMessage());
        response.sendError(HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->{

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }
}
