package fr.framelab.api.model.responses;

public class ErrorResponse {
    protected boolean success;
    protected String token;

    public ErrorResponse(boolean success, String token) {
        this.success = success;
        this.token = token;
    }
}
