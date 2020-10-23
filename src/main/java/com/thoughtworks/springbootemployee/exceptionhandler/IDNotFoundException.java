package com.thoughtworks.springbootemployee.exceptionhandler;

public class IDNotFoundException extends RuntimeException {
    public IDNotFoundException(String message) {
        super(message);
    }
}
