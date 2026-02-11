package fr.framelab.api.exceptions;

public class HttpInternalServerErrorException extends RuntimeException {
    public HttpInternalServerErrorException(String message) {
        super(message);
    }

    public HttpInternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
