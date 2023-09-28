package com.example.userphoto.controllers;

import com.example.userphoto.exceptions.ErrorInputFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ErrorInputFile.class)
    public ResponseEntity<?> handlerInputFile() {
        return new ResponseEntity<>("Error input file!", HttpStatus.BAD_REQUEST);
    }

}
