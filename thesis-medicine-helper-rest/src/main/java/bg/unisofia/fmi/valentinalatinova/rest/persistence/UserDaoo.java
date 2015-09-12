package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import com.google.common.base.Optional;

public interface UserDaoo {

    /**
     * Finds user by username and password.
     *
     * @param username
     * @param password
     * @return User if found or absent if not
     */
    Optional<User> findByUsernameAndPassword(final String username, final String password);
}
