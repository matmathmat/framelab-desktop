package fr.framelab.api.exceptions;

public class HttpForbiddenException extends RuntimeException {
    public HttpForbiddenException(String message) {
        super(message);
    }

    public HttpForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
