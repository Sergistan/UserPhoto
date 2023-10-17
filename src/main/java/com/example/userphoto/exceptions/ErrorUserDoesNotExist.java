package com.example.userphoto.exceptions;

public class ErrorUserDoesNotExist extends RuntimeException {
    public ErrorUserDoesNotExist() {
        super("User does not exist!");
    }
}