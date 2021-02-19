package com.epam.jwd.cafe.service;

import com.epam.jwd.cafe.dao.user.UserDao;
import com.epam.jwd.cafe.dao.user.UserField;
import com.epam.jwd.cafe.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserService {
    public static final UserService INSTANCE = new UserService();
    private final UserDao USER_DAO = UserDao.INSTANCE;

    private UserService() {
    }

    public Optional<User> findByUsername(String username) {
        return findUserByUniqueField(username, UserField.USERNAME);
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return findUserByUniqueField(phoneNumber, UserField.PHONE_NUMBER);
    }

    public Optional<User> findByEmail(String email) {
        return findUserByUniqueField(email, UserField.EMAIL);
    }

    public Optional<String> registerUser(User user) {
        if (!findByUsername(user.getUsername()).isPresent()) {
            if (!findByEmail(user.getEmail()).isPresent()) {
                try {
                    USER_DAO.create(user);
                    return Optional.empty();
                } catch (RuntimeException e) { //todo: DaoException
//                    log.error("UserDao provided an exception");
//                    throw new ServiceException(e);
                }
            }
            return Optional.of("serverMessage.emailAlreadyTaken");
        }
        return Optional.of("serverMessage.usernameAlreadyTaken");
    }

    public Optional<String> loginUser(String username, String password, Map<String, Object> session) {
        Optional<User> userOptional = findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (password.equals(user.getPassword())) { //todo: crypt password
                if (user.isBlocked()) {
                    return Optional.of("serverMessage.blockedAccount");
                }
                session.put("user", user); //todo: create UserDto (ID, Role)
                return Optional.empty();
            }
        }
        return Optional.of("serverMessage.incorrectUsernameOrPassword");
    }

    private Optional<User> findUserByUniqueField(String searchableField, UserField nameOfField) {
        List<User> users = new ArrayList<>();
        try {
            users = USER_DAO.findByField(searchableField, nameOfField);
        } catch (RuntimeException e) { //todo: DaoException
            //todo: log.error("Failed on a user search");
            //todo: throw new ServiceException(e);
        }
        return ((users.size() > 0) ? Optional.of(users.get(0)) : Optional.empty());
    }
}
