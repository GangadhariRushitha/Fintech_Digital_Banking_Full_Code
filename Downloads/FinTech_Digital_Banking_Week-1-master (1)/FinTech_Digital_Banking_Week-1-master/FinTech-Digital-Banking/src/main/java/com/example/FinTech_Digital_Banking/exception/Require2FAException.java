package com.example.FinTech_Digital_Banking.exception;

public class Require2FAException extends RuntimeException {
    public Require2FAException(String message) {
        super(message);
    }
}
