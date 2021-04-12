package com.epam.jwd.cafe.service;

import com.epam.jwd.cafe.dao.field.UserField;
import com.epam.jwd.cafe.dao.impl.UserDao;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.model.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The class provides a business logics of {@link User}.
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class UserService {
    private final Logger LOGGER = LogManager.getLogger(UserService.class);
    public static final UserService INSTANCE = new UserService();
    private final UserDao USER_DAO = UserDao.INSTANCE;

    private UserService() {
    }

    /**
     * Register new user
     *
     * @param user {@link User} object to be register
     * @return {@link Optional<String>} - server message, if email or username is already exist
     * @throws ServiceException if the database access error
     */
    public Optional<String> registerUser(User user) throws ServiceException {
        if (!findByUsername(user.getUsername()).isPresent()) {
            if (!findByEmail(user.getEmail()).isPresent()) {
                try {
                    USER_DAO.create(user);
                    return Optional.empty();
                } catch (DaoException e) {
                    LOGGER.error("Failed to register user: " + user);
                    throw new ServiceException(e);
                }
            }
            return Optional.of("serverMessage.emailAlreadyTaken");
        }
        return Optional.of("serverMessage.usernameAlreadyTaken");
    }

    /**
     * Login user
     *
     * @param username the username
     * @param password the user password
     * @param session  the session of user
     * @return Server message, if user is blocked or incorrect data entered
     * @throws ServiceException if the database access error
     */
    public Optional<String> loginUser(String username, String password, Map<String, Object> session) throws ServiceException {
        Optional<User> userOptional = findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (password.equals(user.getPassword())) {
                if (user.getIsBlocked()) {
                    return Optional.of("serverMessage.blockedAccount");
                }
                UserDto userDto = new UserDto(user.getId(), user.getRole(), user.getIsBlocked());
                session.put("user", userDto);
                return Optional.empty();
            }
        }
        return Optional.of("serverMessage.incorrectUsernameOrPassword");
    }

    /**
     * Update user in database
     *
     * @param user updated object of {@link User}
     * @throws ServiceException if the database access error
     */
    public void updateUser(User user) throws ServiceException {
        try {
            USER_DAO.update(user);
        } catch (DaoException e) {
            LOGGER.error("Failed to update user");
            throw new ServiceException(e);
        }
    }

    /**
     * Find all users in database.
     *
     * @return {@link List} of users
     * @throws DaoException if the database access error
     */
    public List<User> findAllUsers() throws ServiceException {
        List<User> users;
        try {
            users = USER_DAO.findAll();
        } catch (DaoException e) {
            LOGGER.error("Failed to find all users in database");
            throw new ServiceException(e);
        }
        return users;
    }

    public Optional<User> findById(int id) throws ServiceException {
        return findUserByUniqueField(String.valueOf(id), UserField.ID);
    }

    public Optional<User> findByUsername(String username) throws ServiceException {
        return findUserByUniqueField(username, UserField.USERNAME);
    }

    public Optional<User> findByEmail(String email) throws ServiceException {
        return findUserByUniqueField(email, UserField.EMAIL);
    }

    private Optional<User> findUserByUniqueField(String searchableField, UserField nameOfField) throws ServiceException {
        List<User> users;
        try {
            users = USER_DAO.findByField(searchableField, nameOfField);
        } catch (DaoException e) {
            LOGGER.error("Failed on a user search with field = " + nameOfField);
            throw new ServiceException("Failed search user by unique field", e);
        }
        return ((users.size() > 0) ? Optional.of(users.get(0)) : Optional.empty());
    }
}
