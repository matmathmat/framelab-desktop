package fr.framelab.api.exceptions;

public class HttpPaymentRequiredException extends RuntimeException {
    public HttpPaymentRequiredException(String message) {
        super(message);
    }

    public HttpPaymentRequiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
