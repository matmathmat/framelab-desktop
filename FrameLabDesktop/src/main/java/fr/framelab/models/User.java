package fr.framelab.models;

public class User {
    protected int id;
    protected String firstname;
    protected String lastname;
    protected int isAdmin;
    protected String email;
    protected String token;
    protected int score;

    ///<summary>
    /// Basic User
    ///</summary>
    public User(int id, String firstname, String lastname, int isAdmin) {
        if (id < 0) {
            throw new IllegalArgumentException("Negative id is not allowed");
        }

        if (firstname == null) {
            throw new IllegalArgumentException("Null firstName is not allowed");
        }
        if (firstname.isBlank()) {
            throw new IllegalArgumentException("Empty firstName is not allowed");
        }

        if (lastname == null) {
            throw new IllegalArgumentException("Null lastName is not allowed");
        }
        if (lastname.isBlank()) {
            throw new IllegalArgumentException("Empty lastName is not allowed");
        }

        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isAdmin = isAdmin;
        this.score = 0;
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

        return this.firstname;
    }

    public String getLastName() {

        return this.lastname;
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

    public int getScore() {
        return this.score;
    }

    public void setFirstName(String firstName) {

        this.firstname = firstName;
    }

    public void setLastName(String lastName) {

        this.lastname = lastName;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public void setToken(String token) {

        this.token = token;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
