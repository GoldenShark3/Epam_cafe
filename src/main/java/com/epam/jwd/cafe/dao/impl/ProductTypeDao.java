package com.epam.jwd.cafe.dao.impl;

import com.epam.jwd.cafe.dao.AbstractDao;
import com.epam.jwd.cafe.dao.field.EntityField;
import com.epam.jwd.cafe.dao.field.ProductTypeField;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.model.Order;
import com.epam.jwd.cafe.model.ProductType;
import com.epam.jwd.cafe.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The class provide CRUD operations for {@link ProductType}
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ProductTypeDao extends AbstractDao<ProductType> {
    public static final ProductTypeDao INSTANCE = new ProductTypeDao(ConnectionPool.getInstance());

    private static final String SQL_FIND_ALL = "SELECT id, type_name, img_filename FROM product_type";
    private static final String SQL_CREATE = "INSERT INTO product_type (type_name, img_filename) VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE product_type SET type_name = ?, img_filename = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM product_type WHERE id = ?";
    private static final String SQL_AMOUNT = "SELECT count(*) FROM product_type";

    private ProductTypeDao(ConnectionPool connectionPool) {
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
    protected void prepareCreateStatement(PreparedStatement preparedStatement, ProductType entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getFileName());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement preparedStatement, ProductType entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getFileName());
        preparedStatement.setInt(3, entity.getId());
    }

    @Override
    protected Optional<ProductType> parseResultSet(ResultSet resultSet) throws SQLException {
        return Optional.of(
                ProductType.builder()
                        .withId(resultSet.getInt("id"))
                        .withName(resultSet.getString("type_name"))
                        .withFileName(resultSet.getString("img_filename"))
                        .build()
        );
    }

    @Override
    public List<ProductType> findByField(String searchableField, EntityField<ProductType> nameOfField) throws DaoException {
        List<ProductType> productTypeList = new ArrayList<>();
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveSqlByProductTypeField((ProductTypeField) nameOfField))) {
                preparedStatement.setString(1, searchableField);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Optional<ProductType> productTypeOptional = parseResultSet(resultSet);
                    productTypeOptional.ifPresent(productTypeList::add);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find product type by " + nameOfField.toString().toLowerCase());
        }
        return productTypeList;
    }

    private String retrieveSqlByProductTypeField(ProductTypeField nameOfField) throws DaoException {
        StringBuilder sql = new StringBuilder(SQL_FIND_ALL);
        switch (nameOfField) {
            case ID:
                sql.append(" WHERE id = ?");
                break;
            case NAME:
                sql.append(" WHERE type_name = ?");
                break;
            case IMG_FILENAME:
                sql.append(" WHERE img_filename = ?");
                break;
            default:
                throw new DaoException(nameOfField.name().toLowerCase() + " - this field does not exist");
        }
        return sql.toString();
    }
}
