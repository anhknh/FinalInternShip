package com.example.finalinternship.exception;

import java.io.IOException;

public class CustomIOException extends RuntimeException {
    public CustomIOException(IOException cause) {
        super(cause);
    }
}
