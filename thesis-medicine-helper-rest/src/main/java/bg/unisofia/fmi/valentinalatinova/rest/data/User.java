package bg.unisofia.fmi.valentinalatinova.rest.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends DataBaseObject {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean isDoctor;

    public User() {
    }

    @Override
    public void load(ResultSet resultSet) {
        try {
            id = resultSet.getLong("ID");
            username = resultSet.getString("userName");
            password = resultSet.getString("passwordHash");
            firstName = resultSet.getString("firstName");
            lastName = resultSet.getString("lastName");
            isDoctor = resultSet.getLong("doctor_ID") > 0;
        } catch (SQLException e) {
            getLogger().error(e.getMessage());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getIsDoctor() {
        return isDoctor;
    }

    public void setIsDoctor(Boolean isDoctor) {
        this.isDoctor = isDoctor;
    }
}
