package com.epam.jwd.cafe.dao.impl;

import com.epam.jwd.cafe.dao.field.UserField;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.model.Role;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.pool.ConnectionPool;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDaoTest {

    @BeforeAll
    public static void beforeAll() throws ClassNotFoundException {
        ConnectionPool.getInstance().initConnectionPool();
    }

    @Test
    public void successCRUDOperationsTest() throws DaoException {
        UserDao userDao = UserDao.INSTANCE;
        User.Builder userBuilder = User.builder()
                .withUsername("username").withPassword("password")
                .withEmail("email").withIsBlocked(false)
                .withBalance(new BigDecimal("0.00")).withFirstName("firstName")
                .withLastName("lastName").withRole(Role.USER)
                .withLoyaltyPoints(0).withPhoneNumber("+375333132590")
                .withId(1);

        userDao.create(userBuilder.build());
        List<User> createUsers = userDao.findByField("username", UserField.USERNAME);
        userBuilder.withId(createUsers.get(0).getId());
        assertTrue(createUsers.contains(userBuilder.build()));

        userBuilder.withEmail("updateEmail");
        userDao.update(userBuilder.build());
        List<User> updateUsers = userDao.findByField("updateEmail", UserField.EMAIL);
        assertTrue(updateUsers.contains(userBuilder.build()));

        userDao.deleteById(updateUsers.get(0).getId());
        List<User> deleteUsers = userDao.findByField("updateEmail", UserField.USERNAME);
        assertFalse(deleteUsers.contains(userBuilder.build()));
    }

    @AfterAll
    public static void afterAll() throws SQLException {
        ConnectionPool.getInstance().destroyConnectionPool(false);
    }
}