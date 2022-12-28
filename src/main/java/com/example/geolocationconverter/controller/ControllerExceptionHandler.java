package com.example.geolocationconverter.controller;

import com.example.geolocationconverter.exception.IncompleteAddressException;
import com.example.geolocationconverter.exception.InvalidRequestStatusException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequestMapping(consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = IncompleteAddressException.class)
    @ResponseBody
    public final ResponseEntity<Object> handleConstraintViolationEx(IncompleteAddressException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));
        return new ResponseEntity<>(ex.getMessage(), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidRequestStatusException.class)
    @ResponseBody
    public final ResponseEntity<Object> handleConstraintViolationEx(InvalidRequestStatusException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));
        return new ResponseEntity<>(ex.getMessage(), headers, HttpStatus.BAD_REQUEST);
    }



}