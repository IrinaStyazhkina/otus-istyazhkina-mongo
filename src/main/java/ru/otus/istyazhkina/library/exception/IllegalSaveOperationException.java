package ru.otus.istyazhkina.library.exception;

public class IllegalSaveOperationException extends RuntimeException {

    public IllegalSaveOperationException() {
    }

    public IllegalSaveOperationException(String message) {
        super(message);
    }

    public IllegalSaveOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalSaveOperationException(Throwable cause) {
        super(cause);
    }

    public IllegalSaveOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
