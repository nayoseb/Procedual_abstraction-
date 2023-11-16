package com.tip.functional.customexception;

public class IteratorMaxSizeNegativeException extends IllegalArgumentException {
    public IteratorMaxSizeNegativeException(String message) {
        super(message);
    }
}
