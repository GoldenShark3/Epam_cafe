package com.epam.jwd.cafe.dao.impl;

import com.epam.jwd.cafe.dao.AbstractDao;
import com.epam.jwd.cafe.dao.field.EntityField;
import com.epam.jwd.cafe.dao.field.ProductField;
import com.epam.jwd.cafe.dao.field.ProductTypeField;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.model.ProductType;
import com.epam.jwd.cafe.pool.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductDao extends AbstractDao<Product> {
    public static final ProductDao INSTANCE = new ProductDao(ConnectionPool.getInstance());

    private static final String SQL_FIND_ALL = "SELECT id, product_name, price, img_name," +
            " product_description, type_id FROM product";

    private static final String SQL_CREATE = "INSERT INTO product(product_name, price, img_name," +
            " product_description, type_id) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_DELETE = "DELETE FROM product WHERE id = ?";

    private static final String SQL_UPDATE = "UPDATE product SET product_name = ?, price = ?, img_name = ?," +
            " product_description = ?, type_id = ? WHERE id = ?";

    private static final String SQL_FIND_BY_ORDER_ID = "SELECT id, product_name, price, img_name, product_description, " +
            "type_id, amount FROM product INNER JOIN order_product as op on product.id = op.product_id " +
            "WHERE op.order_id = ?";

    private ProductDao(ConnectionPool connectionPool) {
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
    protected void prepareCreateStatement(PreparedStatement preparedStatement, Product entity) throws SQLException {
        prepareAllProductStatements(preparedStatement, entity);
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement preparedStatement, Product entity) throws SQLException {
        prepareAllProductStatements(preparedStatement, entity);
        preparedStatement.setInt(6, entity.getId());
    }

    @Override
    protected Optional<Product> parseResultSet(ResultSet resultSet) throws SQLException, DaoException {
        Product product;
        ProductType productType = retrieveProductTypeById(resultSet.getInt("type_id"));

        product = Product.builder()
                .withId(resultSet.getInt("id"))
                .withName(resultSet.getString("product_name"))
                .withPrice(resultSet.getBigDecimal("price"))
                .withImgFileName(resultSet.getString("img_name"))
                .withDescription(resultSet.getString("product_description"))
                .withProductType(productType)
                .build();

        return Optional.of(product);
    }

    @Override
    public List<Product> findByField(String searchableField, EntityField<Product> nameOfField) throws DaoException {
        List<Product> productList = new ArrayList<>();
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveSqlByProductField((ProductField) nameOfField))) {
                preparedStatement.setString(1, searchableField);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Optional<Product> productOptional = parseResultSet(resultSet);
                    productOptional.ifPresent(productList::add);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find product by " + nameOfField.toString().toLowerCase());
        }
        return productList;
    }

    protected Map<Product, Integer> findProductsInOrder(int id) throws DaoException {
        Map<Product, Integer> products = new HashMap<>();
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ORDER_ID)) {
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Optional<Product> productOptional = parseResultSet(resultSet);
                    Integer amount = resultSet.getInt("amount");
                    productOptional.ifPresent(product -> products.put(product, amount));
                }
            }
        } catch (SQLException e) {
            //todo: log.error("Failed to find products in order by id in product dao");
            throw new DaoException("Failed to find products in order by id");
        }
        return products;
    }

    private void prepareAllProductStatements(PreparedStatement preparedStatement, Product product) throws SQLException {
        preparedStatement.setString(1, product.getName());
        preparedStatement.setBigDecimal(2, product.getPrice());
        preparedStatement.setString(3, product.getImgFileName());
        preparedStatement.setString(4, product.getDescription());
        preparedStatement.setInt(5, product.getProductType().getId());
    }

    private ProductType retrieveProductTypeById(int id) throws DaoException {
        List<ProductType> productTypeList = ProductTypeDao.INSTANCE.findByField(String.valueOf(id), ProductTypeField.ID);
        if (productTypeList.size() < 1) {
            //todo: log.warn("Failed to load product type in productDao");
            throw new DaoException("Failed to load product type");
        }
        return productTypeList.get(0);
    }


    private String retrieveSqlByProductField(ProductField nameOfField) throws DaoException {
        StringBuilder sql = new StringBuilder(SQL_FIND_ALL);
        switch (nameOfField) {
            case ID:
                sql.append(" WHERE id = ?");
                break;
            case NAME:
                sql.append(" WHERE product_name = ?");
                break;
            case PRICE:
                sql.append(" WHERE price = ?");
                break;
            case IMG_NAME:
                sql.append(" WHERE img_name = ?");
                break;
            case PRODUCT_DESCRIPTION:
                sql.append(" WHERE product_description = ?");
                break;
            default:
                throw new DaoException(nameOfField.name().toLowerCase() + " - this field does not exist");
        }
        return sql.toString();
    }

}
