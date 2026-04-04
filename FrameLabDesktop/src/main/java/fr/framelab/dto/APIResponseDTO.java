package fr.framelab.dto;

public class APIResponseDTO<T> {
    protected boolean success;
    protected T result;

    public APIResponseDTO(boolean success, T result) {
        this.success = success;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getResult() {
        return result;
    }
}
