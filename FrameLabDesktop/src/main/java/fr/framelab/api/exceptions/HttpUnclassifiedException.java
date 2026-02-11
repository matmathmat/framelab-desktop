package fr.framelab.api.exceptions;

public class HttpUnclassifiedException extends RuntimeException {
    public HttpUnclassifiedException(String message) {
        super(message);
    }

    public HttpUnclassifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
