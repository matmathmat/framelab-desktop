package fr.framelab.api.model.user;

public class CompleteUser {
    protected int id;
    protected String firstName;
    protected String lastName;
    protected boolean isAdmin;
    protected String email;

    public CompleteUser(int id, String firstName, String lastName, boolean isAdmin, String email) {
        if (id < 0) {
            throw new IllegalArgumentException("Negative id is not allowed");
        }

        if (firstName == null) {
            throw new IllegalArgumentException("Null firstName is not allowed");
        }
        if (firstName.isBlank()) {
            throw new IllegalArgumentException("Empty firstName is not allowed");
        }

        if (lastName == null) {
            throw new IllegalArgumentException("Null lastName is not allowed");
        }
        if (lastName.isBlank()) {
            throw new IllegalArgumentException("Empty lastName is not allowed");
        }

        if (email == null) {
            throw new IllegalArgumentException("Null email is not allowed");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("Empty email is not allowed");
        }

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
        this.email = email;
    }

    public int getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public String getEmail() {
        return this.email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
