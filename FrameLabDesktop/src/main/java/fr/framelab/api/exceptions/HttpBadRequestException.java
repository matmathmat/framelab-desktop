package fr.framelab.api.exceptions;

public class HttpBadRequestException extends RuntimeException {
    public HttpBadRequestException(String message) {
        super(message);
    }

    public HttpBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
