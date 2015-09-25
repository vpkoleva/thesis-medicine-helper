package bg.unisofia.fmi.valentinalatinova.rest.data;

public class User {
    private long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private static User noAuthUser = new User();

    private User() {
        this.id = 1;
    }

    public User(long id, String username, String password, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static User getNoAuthUser() {
        return noAuthUser;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
