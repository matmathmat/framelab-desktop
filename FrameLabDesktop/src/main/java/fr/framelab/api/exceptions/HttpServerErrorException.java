package fr.framelab.api.exceptions;

///<summary>
/// 5xx - Server/Application Server Error
///</summary>
public class HttpServerErrorException extends RuntimeException {
    public HttpServerErrorException(String message) {
        super(message);
    }

    public HttpServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
