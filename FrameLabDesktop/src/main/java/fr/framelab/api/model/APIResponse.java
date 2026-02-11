package fr.framelab.api.model;

public class APIResponse<T> {
    protected boolean success;
    protected T result;

    public APIResponse(boolean success, T result) {
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
