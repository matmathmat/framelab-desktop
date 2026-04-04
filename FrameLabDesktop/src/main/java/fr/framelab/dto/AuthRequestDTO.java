package fr.framelab.dto;

public class AuthRequestDTO {
    protected String email;
    protected String password;

    public AuthRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
