package com.jpmc.theater.exceptions;

public class TheaterException extends RuntimeException{

    public TheaterException() {};

    public TheaterException(String message) {
        super(message);
    }
}
