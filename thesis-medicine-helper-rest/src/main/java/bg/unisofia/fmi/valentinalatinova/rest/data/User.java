package bg.unisofia.fmi.valentinalatinova.rest.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends DataBaseObject {
    private long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private static User noAuthUser;

    public User() {
    }

    public User(long id, String username, String password, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static User getNoAuthUser() {
        if (noAuthUser == null) {
            noAuthUser = new User();
            noAuthUser.id = 1;
        }
        return noAuthUser;
    }

    @Override
    public void load(ResultSet resultSet) {
        try {
            id = resultSet.getInt("ID");
            username = resultSet.getString("userName");
            password = resultSet.getString("passwordHash");
            firstName = resultSet.getString("firstName");
            lastName = resultSet.getString("lastName");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
