package bg.unisofia.fmi.valentinalatinova.rest.persistence.impl;

import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.UserDaoo;
import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.Map;

public class UserDaoImpl implements UserDaoo {
    final static Map<Long, User> userTable = new HashMap<>();

    static {
        userTable.put(1l, new User(2, "user1", "password"));
        userTable.put(2l, new User(3, "user2", "password"));
    }

    @Override
    public Optional<User> findByUsernameAndPassword(final String username, final String password) {
        for (Map.Entry<Long, User> entry : userTable.entrySet()) {
            User user = entry.getValue();
            if (user.getPassword().equals(password) && user.getUsername().equals(username)) {
                return Optional.of(user);
            }
        }
        return Optional.absent();
    }
}
