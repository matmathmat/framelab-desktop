package fr.framelab.exceptions.http;

public class HttpBadRequestException extends RuntimeException {
    public HttpBadRequestException(String message) {
        super(message);
    }

    public HttpBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
