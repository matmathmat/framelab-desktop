package fr.framelab.api.exceptions;

///<summary>
/// 4xx - HTTP Client Error
///</summary>
public class HttpClientErrorException extends RuntimeException {
    public HttpClientErrorException(String message) {
        super(message);
    }

    public HttpClientErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
