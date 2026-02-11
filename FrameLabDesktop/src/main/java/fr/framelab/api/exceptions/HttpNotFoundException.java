package fr.framelab.api.exceptions;

public class HttpNotFoundException extends RuntimeException {
    public HttpNotFoundException(String message) {
        super(message);
    }

    public HttpNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
