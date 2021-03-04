package com.epam.jwd.cafe.service;

import com.epam.jwd.cafe.dao.field.UserField;
import com.epam.jwd.cafe.dao.impl.UserDao;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.model.dto.UserDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserService {
    public static final UserService INSTANCE = new UserService();
    private final UserDao USER_DAO = UserDao.INSTANCE;

    private UserService() {
    }

    public Optional<String> registerUser(User user) throws ServiceException {
        if (!findByUsername(user.getUsername()).isPresent()) {
            if (!findByEmail(user.getEmail()).isPresent()) {
                try {
                    USER_DAO.create(user);
                    return Optional.empty();
                } catch (DaoException e) {
//                    log.error("UserDao provided an exception");
                    throw new ServiceException(e);
                }
            }
            return Optional.of("serverMessage.emailAlreadyTaken");
        }
        return Optional.of("serverMessage.usernameAlreadyTaken");
    }

    public Optional<String> loginUser(String username, String password, Map<String, Object> session) throws ServiceException {
        Optional<User> userOptional = findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (password.equals(user.getPassword())) {
                if (user.isBlocked()) {
                    return Optional.of("serverMessage.blockedAccount");
                }
                UserDto userDto = new UserDto(user.getId(), user.getRole());
                session.put("user", userDto);
                return Optional.empty();
            }
        }
        return Optional.of("serverMessage.incorrectUsernameOrPassword");
    }

    public List<User> findAllUsers() throws DaoException {
        return USER_DAO.findAll();
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

    public Optional<User> findByPhoneNumber(String phoneNumber) throws ServiceException {
        return findUserByUniqueField(phoneNumber, UserField.PHONE_NUMBER);
    }

    private Optional<User> findUserByUniqueField(String searchableField, UserField nameOfField) throws ServiceException {
        List<User> users;
        try {
            users = USER_DAO.findByField(searchableField, nameOfField);
        } catch (DaoException e) {
            //todo: log.error("Failed on a user search");
            throw new ServiceException("Failed search user by unique field", e);
        }
        return ((users.size() > 0) ? Optional.of(users.get(0)) : Optional.empty());
    }
}
