package com.epam.jwd.cafe.dao;

import com.epam.jwd.cafe.model.BaseEntity;
import com.epam.jwd.cafe.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T extends BaseEntity> implements Dao<T> {
    protected final ConnectionPool connectionPool;

    protected AbstractDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    protected abstract Optional<T> parseResultSet(ResultSet resultSet) throws SQLException;

    protected abstract String getFindAllSql();

    protected abstract String getCreateSql();

    protected abstract String getUpdateSql();

    protected abstract String getDeleteSql();

    protected abstract void prepareCreateStatement(PreparedStatement preparedStatement, T entity) throws SQLException;

    protected abstract void prepareUpdateStatement(PreparedStatement preparedStatement, T entity) throws SQLException;

    @Override
    public void create(final T entity) {
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(getCreateSql())) {
                prepareCreateStatement(preparedStatement, entity);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            //todo: throw new DaoException("Failed to create entity", e);
        }
    }

    @Override
    public T update(final T entity) {
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(getUpdateSql())) {
                prepareUpdateStatement(preparedStatement, entity);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            //todo: throw new DaoException("Failed to update entity", e);
        }
        return entity;
    }

    @Override
    public void deleteById(int id) {
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    getDeleteSql() + id)) {
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            //todo: throw new DaoException("Failed to delete entity with id = " + id, e);
        }
    }
}
