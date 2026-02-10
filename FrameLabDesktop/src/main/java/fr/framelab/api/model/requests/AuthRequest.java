package fr.framelab.api.model.requests;

public class AuthRequest {
    protected String email;
    protected String password;

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
