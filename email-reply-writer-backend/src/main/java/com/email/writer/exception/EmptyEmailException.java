package com.email.writer.exception;

public class EmptyEmailException extends IllegalArgumentException {
    public EmptyEmailException(String msg) {
        super(msg);
    }
}
