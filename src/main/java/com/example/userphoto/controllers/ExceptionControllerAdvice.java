package com.example.userphoto.controllers;

import com.example.userphoto.exceptions.ErrorInputFile;
import com.example.userphoto.exceptions.ErrorUserDoesNotExist;
import com.example.userphoto.exceptions.ErrorUserForbidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ErrorInputFile.class)
    public ResponseEntity<?> handlerInputFile(ErrorInputFile errorInputFile) {
        return new ResponseEntity<>(errorInputFile.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorUserDoesNotExist.class)
    public ResponseEntity<?> handlerErrorUserDoesNotExist(ErrorUserDoesNotExist errorUserDoesNotExist) {
        return new ResponseEntity<>(errorUserDoesNotExist.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorUserForbidden.class)
    public ResponseEntity<?> handlerErrorUserForbidden(ErrorUserForbidden errorUserForbidden) {
        return new ResponseEntity<>(errorUserForbidden.getMessage(), HttpStatus.FORBIDDEN);
    }

}
