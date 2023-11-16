package com.tip.functional.customexception;

public class IllegalNullArgumentException extends IllegalArgumentException {
    public IllegalNullArgumentException(String message) {
        super(message);
    }
}
