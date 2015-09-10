package bg.unisofia.fmi.valentinalatinova.rest.data;

public class User {
    private long id;
    private String username;
    private String password;
    private static User noAuthUser = new User(1, "System", "System");

    private User() {
    }

    public User(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
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
}
