package bg.unisofia.fmi.valentinalatinova.rest.persistence.impl;

import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.UserDao;
import com.google.common.base.Optional;

import java.util.List;

public class UserDaoImpl implements UserDao {

    private DataBaseCommander dataBaseCommander;

    public UserDaoImpl(DataBaseCommander dataBaseCommander) {
        this.dataBaseCommander = dataBaseCommander;
    }

    @Override
    public Optional<User> findByUsernameAndPassword(final String username, final String password) {
        String sql = "SELECT * FROM `users` WHERE `userName`=? AND `passwordHash`=?";
        List<User> users = dataBaseCommander.select(User.class, sql, username, password);
        if (users.size() == 1) {
            return Optional.of(users.get(0));
        }
        return Optional.absent();
    }
}
