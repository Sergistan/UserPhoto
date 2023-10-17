package com.example.userphoto.exceptions;

public class ErrorUserForbidden extends RuntimeException{
    public ErrorUserForbidden() {
        super("User forbidden access!");
    }
}
