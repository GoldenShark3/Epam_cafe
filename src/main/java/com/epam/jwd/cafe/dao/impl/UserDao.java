package com.epam.jwd.cafe.dao.impl;

import com.epam.jwd.cafe.dao.AbstractDao;
import com.epam.jwd.cafe.dao.field.EntityField;
import com.epam.jwd.cafe.dao.field.UserField;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.model.Order;
import com.epam.jwd.cafe.model.Role;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The class provide CRUD operations for {@link User}
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class UserDao extends AbstractDao<User> {
    public static UserDao INSTANCE = new UserDao(ConnectionPool.getInstance());

    private static final String SQL_FIND_ALL = "SELECT cu.id, username, pass, first_name, last_name," +
            "email, balance, loyalty_points, is_blocked, phone_number, ur.role_name FROM cafe_user as cu" +
            " INNER JOIN user_role AS ur ON ur.id = cu.role_id";

    private static final String SQL_CREATE = "INSERT INTO cafe_user(username, pass, first_name, last_name," +
            "email, balance, loyalty_points, is_blocked, phone_number, role_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "UPDATE cafe_user SET username = ?, pass = ?, first_name = ?, last_name = ?," +
            "email = ?, balance = ?, loyalty_points = ?, is_blocked = ?, phone_number = ?, role_id = ? WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM cafe_user WHERE id = ?";

    private UserDao(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    @Override
    protected String getFindAllSql() {
        return SQL_FIND_ALL;
    }

    @Override
    protected String getCreateSql() {
        return SQL_CREATE;
    }

    @Override
    protected String getUpdateSql() {
        return SQL_UPDATE;
    }

    @Override
    protected String getDeleteSql() {
        return SQL_DELETE;
    }

    @Override
    protected void prepareCreateStatement(PreparedStatement preparedStatement, User entity) throws SQLException {
        preparedAllUserStatements(preparedStatement, entity);
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement preparedStatement, User entity) throws SQLException {
        preparedAllUserStatements(preparedStatement, entity);
        preparedStatement.setInt(11, entity.getId());
    }

    private void preparedAllUserStatements(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getFirstName());
        preparedStatement.setString(4, user.getLastName());
        preparedStatement.setString(5, user.getEmail());
        preparedStatement.setBigDecimal(6, user.getBalance());
        preparedStatement.setInt(7, user.getLoyaltyPoints());
        preparedStatement.setBoolean(8, user.getIsBlocked());
        preparedStatement.setString(9, user.getPhoneNumber());
        preparedStatement.setInt(10, user.getRole().ordinal() + 1);
    }

    @Override
    protected Optional<User> parseResultSet(ResultSet resultSet) throws SQLException {
        User baseUser;
        baseUser = User.builder()
                .withId(resultSet.getInt("id"))
                .withUsername(resultSet.getString("username"))
                .withPassword(resultSet.getString("pass"))
                .withFirstName(resultSet.getString("first_name"))
                .withLastName(resultSet.getString("last_name"))
                .withRole(Role.valueOf(resultSet.getString("role_name")))
                .withBalance(resultSet.getBigDecimal("balance"))
                .withEmail(resultSet.getString("email"))
                .withLoyaltyPoints(resultSet.getInt("loyalty_points"))
                .withPhoneNumber(resultSet.getString("phone_number"))
                .withIsBlocked(resultSet.getBoolean("is_blocked"))
                .build();
        return Optional.of(baseUser);
    }

    @Override
    public List<User> findByField(String searchableField, EntityField<User> nameOfField) throws DaoException {
        List<User> usersList = new ArrayList<>();
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveSqlByUserField((UserField) nameOfField))) {
                preparedStatement.setString(1, searchableField);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Optional<User> userOptional = parseResultSet(resultSet);
                    userOptional.ifPresent(usersList::add);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by " + nameOfField.toString().toLowerCase());
        }
        return usersList;
    }

    private String retrieveSqlByUserField(UserField nameOfField) throws DaoException {
        StringBuilder sql = new StringBuilder(SQL_FIND_ALL);
        switch (nameOfField) {
            case ID:
                sql.append(" WHERE cu.id = ?");
                break;
            case USERNAME:
                sql.append(" WHERE username = ?");
                break;
            case FIRSTNAME:
                sql.append(" WHERE first_name = ?");
                break;
            case LASTNAME:
                sql.append(" WHERE last_name = ?");
                break;
            case PHONE_NUMBER:
                sql.append(" WHERE phone_number = ?");
                break;
            case EMAIL:
                sql.append(" WHERE email = ?");
                break;
            default:
                throw new DaoException(nameOfField.name().toLowerCase() + " - this field does not exist");
        }
        return sql.toString();
    }
}
