package fr.framelab.exceptions.http;

public class HttpUnauthorizedException extends RuntimeException {
    public HttpUnauthorizedException(String message) {
        super(message);
    }

    public HttpUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
