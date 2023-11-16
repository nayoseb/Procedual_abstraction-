package com.tip.functional.customexception;

public class UnsupportedInfiniteIteratorException extends IllegalArgumentException {
    public UnsupportedInfiniteIteratorException(String message) {
        super(message);
    }
}
