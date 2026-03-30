package fr.framelab.exceptions.http;

public class HttpUnclassifiedException extends RuntimeException {
    public HttpUnclassifiedException(String message) {
        super(message);
    }

    public HttpUnclassifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
