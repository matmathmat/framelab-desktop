package fr.framelab.api.model.user;

public class User {
    protected int id;
    protected String firstName;
    protected String lastName;
    protected int isAdmin;
    protected String email;
    protected String token;

    ///<summary>
    /// Basic User
    ///</summary>
    public User(int id, String firstName, String lastName, int isAdmin) {
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

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
    }

    ///<summary>
    /// Complete User
    ///</summary>
    public User(int id, String firstName, String lastName, int isAdmin, String email) {
        this(id, firstName, lastName, isAdmin);

        if (email == null) {
            throw new IllegalArgumentException("Null email is not allowed");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("Empty email is not allowed");
        }

        String emailRegex = "^[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        this.email = email;
    }

    ///<summary>
    /// Complete User + Token
    ///</summary>
    public User(int id, String firstName, String lastName, int isAdmin, String email, String token) {
        this(id, firstName, lastName, isAdmin, email);

        if (token == null) {
            throw new IllegalArgumentException("Null token is not allowed");
        }
        if (token.isBlank()) {
            throw new IllegalArgumentException("Empty token is not allowed");
        }

        this.token = token;
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

    public String getEmail() {
        return this.email;
    }

    public boolean getIsAdmin() {
        return (this.isAdmin != 0);
    }

    public String getToken() {
        return token;
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
