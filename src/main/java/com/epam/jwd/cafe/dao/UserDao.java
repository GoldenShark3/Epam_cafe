package com.epam.jwd.cafe.dao;

import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserDao implements Dao<User>{
    private static final String SQL_FIND_ALL = "SELECT cu.id, username, pass, first_name, last_name," +
            "email, balance, loyalty_points, is_blocked, phone_number, ur.role_name FROM cafe_user as cu" +
            "INNER JOIN user_role AS ur ON ur.id = cu.role_id";

    private static final String SQL_CREATE = "INSERT INTO cafe_user(username, pass, first_name, last_name," +
            "email, balance, loyalty_points, is_blocked, phone_number, role_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "UPDATE cafe_user SET username = ?, pass = ?, first_name = ?, last_name = ?," +
            "email = ?, balance = ?, loyalty_points = ?, is_blocked = ?, phone_number = ?, role_id = ? WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM cafe_user as cu";

    @Override
    public void create(User entity) {
        try(Connection connection = ConnectionPool.getInstance().retrieveConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE);

            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setString(3, entity.getFirstName());
            preparedStatement.setString(4, entity.getLastName());
            preparedStatement.setString(5, entity.getEmail());
            preparedStatement.setBigDecimal(6, entity.getBalance());
            preparedStatement.setInt(7, entity.getLoyaltyPoints());
            preparedStatement.setBoolean(8, entity.isBlocked());
            preparedStatement.setString(9, entity.getPhoneNumber());
            preparedStatement.setInt(10, entity.getRole().ordinal() + 1);
        } catch (SQLException e) {
            //todo: log and throw DaoException
        }
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public List<User> findAll() {
        return null;
    }
}
