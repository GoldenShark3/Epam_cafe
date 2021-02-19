package com.epam.jwd.cafe.dao.user;

import com.epam.jwd.cafe.dao.AbstractDao;
import com.epam.jwd.cafe.model.Role;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        preparedStatement.setBoolean(8, user.isBlocked());
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
                .build();
        return Optional.of(baseUser);
    }

    @Override
    public List<User> findByField(String searchableField, UserField nameOfField) {
        List<User> usersList = new ArrayList<>();
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(retrieveSqlByUserField(nameOfField, searchableField));
                while (resultSet.next()) {
                    Optional<User> userOptional = parseResultSet(resultSet);
                    userOptional.ifPresent(usersList::add);
                }
            }
        } catch (SQLException e) {
            //todo: throw new DaoException();
        }
        return usersList;
    }

    private String retrieveSqlByUserField(UserField field, String searchableField) {
        String sql = SQL_FIND_ALL;
        switch (field) {
            case USERNAME:
                sql += " WHERE username = " + searchableField;
                break;
            case FIRSTNAME:
                sql += " WHERE first_name = " + searchableField;
                break;
            case LASTNAME:
                sql += " WHERE last_name = " + searchableField;
                break;
            case PHONE_NUMBER:
                sql += " WHERE phone_number = " + searchableField;
                break;
            case EMAIL:
                sql += " WHERE email = " + searchableField;
                break;
            case ALL_FIELD:
            default:
                return sql;
        }
        return sql;
    }
}
