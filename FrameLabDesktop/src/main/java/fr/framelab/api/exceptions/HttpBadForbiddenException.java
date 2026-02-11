package fr.framelab.api.exceptions;

public class HttpBadForbiddenException extends RuntimeException {
    public HttpBadForbiddenException(String message) {
        super(message);
    }

    public HttpBadForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
