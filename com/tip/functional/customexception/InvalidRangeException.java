package com.tip.functional.customexception;

public class InvalidRangeException extends IllegalArgumentException {
    public InvalidRangeException(String message) {
        super(message);
    }
}