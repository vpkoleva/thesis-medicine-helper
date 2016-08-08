package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import com.google.common.base.Optional;

import java.util.List;

import bg.unisofia.fmi.valentinalatinova.rest.data.User;

public class UserDAO {

    private DataBaseCommander dataBaseCommander;

    public UserDAO(DataBaseCommander dataBaseCommander) {
        this.dataBaseCommander = dataBaseCommander;
    }

    public Optional<User> findByUsernameAndPassword(final String username, final String password) {
        String sql = "SELECT * FROM `users` WHERE `userName`=? AND `passwordHash`=?";
        List<User> users = dataBaseCommander.select(User.class, sql, username, password);
        if (users.size() == 1) {
            return Optional.of(users.get(0));
        }
        return Optional.absent();
    }
}
