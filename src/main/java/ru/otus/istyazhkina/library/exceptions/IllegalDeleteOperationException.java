package ru.otus.istyazhkina.library.exceptions;

public class IllegalDeleteOperationException extends RuntimeException {
    public IllegalDeleteOperationException() {
    }

    public IllegalDeleteOperationException(String message) {
        super(message);
    }

    public IllegalDeleteOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDeleteOperationException(Throwable cause) {
        super(cause);
    }

    public IllegalDeleteOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
