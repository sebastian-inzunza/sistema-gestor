package com.sistem.sistema.controllers;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.sistem.sistema.exception.NotFoundException;
import com.sistem.sistema.models.Error;

@RestControllerAdvice
public class HandlerExceptionController {


    @ExceptionHandler({
        NullPointerException.class,
        HttpMessageNotWritableException.class,
        NoHandlerFoundException.class,
         NotFoundException.class
    })
    public ResponseEntity<Error> notFoundeException(Exception ex){
        Error error = new Error();
        error.setDate(new Date());
        error.setError("Discrepancia detectada");
        error.setMessage(ex.getMessage());


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }


}
