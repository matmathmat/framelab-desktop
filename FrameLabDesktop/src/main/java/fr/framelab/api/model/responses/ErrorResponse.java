package fr.framelab.api.model.responses;

public class ErrorResponse {
    protected boolean success;
    protected String message;

    public ErrorResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
