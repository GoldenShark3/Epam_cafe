package com.epam.jwd.cafe.dao;

import com.epam.jwd.cafe.dao.field.EntityField;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.model.BaseEntity;
import com.epam.jwd.cafe.pool.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T extends BaseEntity> implements Dao<T> {
    protected final ConnectionPool connectionPool;

    protected AbstractDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    protected abstract String getFindAllSql();

    protected abstract String getCreateSql();

    protected abstract String getUpdateSql();

    protected abstract String getDeleteSql();

    protected abstract void prepareCreateStatement(PreparedStatement preparedStatement, T entity) throws SQLException;

    protected abstract void prepareUpdateStatement(PreparedStatement preparedStatement, T entity) throws SQLException;

    protected abstract Optional<T> parseResultSet(ResultSet resultSet) throws SQLException, DaoException;

    public abstract List<T> findByField(String searchableField, EntityField<T> nameOfField) throws DaoException;

    @Override
    public void create(final T entity) throws DaoException {
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(getCreateSql())) {
                prepareCreateStatement(preparedStatement, entity);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to create entity", e);
        }
    }

    @Override
    public T update(final T entity) throws DaoException {
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(getUpdateSql())) {
                prepareUpdateStatement(preparedStatement, entity);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to update entity", e);
        }
        return entity;
    }

    @Override
    public void deleteById(int id) throws DaoException {
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    getDeleteSql())) {
                preparedStatement.setInt(1, id);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to delete entity with id = " + id, e);
        }
    }

    @Override
    public List<T> findAll() throws DaoException {
        List<T> entitiesList = new ArrayList<>();
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(getFindAllSql())) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    Optional<T> entityOptional = parseResultSet(resultSet);
                    entityOptional.ifPresent(entitiesList::add);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find all entities", e);
        }
        return entitiesList;
    }

}
