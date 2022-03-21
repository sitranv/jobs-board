package com.jobs.sitran.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExistException extends Exception {

    public ExistException() {
    }

    public ExistException(String name, String message) {
        super(name + " " + message);
    }
}
