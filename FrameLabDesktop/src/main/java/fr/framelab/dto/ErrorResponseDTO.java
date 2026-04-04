package fr.framelab.dto;

public class ErrorResponseDTO {
    protected boolean success;
    protected String message;

    public ErrorResponseDTO(boolean success, String message) {
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
