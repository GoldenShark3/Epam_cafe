package com.epam.jwd.cafe.dao.impl;

import com.epam.jwd.cafe.dao.AbstractDao;
import com.epam.jwd.cafe.dao.field.EntityField;
import com.epam.jwd.cafe.dao.field.OrderField;
import com.epam.jwd.cafe.dao.field.UserField;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.exception.EntityNotFoundException;
import com.epam.jwd.cafe.model.Order;
import com.epam.jwd.cafe.model.OrderStatus;
import com.epam.jwd.cafe.model.PaymentMethod;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The class provide CRUD operations for {@link Order}
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class OrderDao extends AbstractDao<Order> {
    private final Logger LOGGER = LogManager.getLogger(OrderDao.class);
    public static final OrderDao INSTANCE = new OrderDao(ConnectionPool.getInstance());

    private static final String SQL_FIND_ALL = "SELECT id, delivery_address, order_cost, create_date," +
            " delivery_date, payment_method_id, status_id, user_id FROM cafe_order";

    private static final String SQL_CREATE = "INSERT INTO cafe_order(delivery_address, order_cost, create_date," +
            " delivery_date, payment_method_id, status_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "UPDATE cafe_order SET delivery_address = ?, order_cost = ?," +
            " create_date = ?, delivery_date = ?, payment_method_id = ?, status_id = ?, user_id = ? WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM cafe_order WHERE id = ?";

    private static final String SQL_CREATE_ORDER_PRODUCTS = "INSERT INTO order_product(order_id, product_id," +
            " amount) VALUES (?, ?, ?)";

    private static final String SQL_DELETE_ORDER_PRODUCTS = "DELETE FROM order_product where product_id = ?";


    private OrderDao(ConnectionPool connectionPool) {
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
    public void create(Order entity) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().retrieveConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatementCreateOrder = null;
            PreparedStatement preparedStatementAddProducts = null;
            try {
                preparedStatementCreateOrder = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                prepareAllOrderStatements(preparedStatementCreateOrder, entity);
                preparedStatementCreateOrder.executeUpdate();
                ResultSet resultSet = preparedStatementCreateOrder.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    for (Map.Entry<Product, Integer> entry : entity.getProducts().entrySet()) {
                        preparedStatementAddProducts = connection.prepareStatement(SQL_CREATE_ORDER_PRODUCTS);
                        preparedStatementAddProducts.setLong(1, id);
                        preparedStatementAddProducts.setInt(2, entry.getKey().getId());
                        preparedStatementAddProducts.setInt(3, entry.getValue());
                        preparedStatementAddProducts.execute();
                        preparedStatementAddProducts.close();
                    }
                    connection.commit();
                    preparedStatementCreateOrder.close();
                } else {
                    throw new SQLException();
                }
            } catch (SQLException e) {
                LOGGER.error("Failed to create order");
                connection.rollback();
                throw new DaoException(e);
            } finally {
                if (preparedStatementCreateOrder != null) {
                    preparedStatementCreateOrder.close();
                }
                if (preparedStatementAddProducts != null) {
                    preparedStatementAddProducts.close();
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to create order");
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareCreateStatement(PreparedStatement preparedStatement, Order entity) throws SQLException {
        prepareAllOrderStatements(preparedStatement, entity);
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement preparedStatement, Order entity) throws SQLException {
        prepareAllOrderStatements(preparedStatement, entity);
        preparedStatement.setInt(8, entity.getId());
    }

    @Override
    protected Optional<Order> parseResultSet(ResultSet resultSet) throws SQLException, DaoException {
        Order order;
        try {
            order = Order.builder()
                    .withId(resultSet.getInt("id"))
                    .withDeliveryAddress(resultSet.getString("delivery_address"))
                    .withCost(resultSet.getBigDecimal("order_cost"))
                    .withCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime())
                    .withDeliveryDate(resultSet.getTimestamp("delivery_date").toLocalDateTime())
                    .withPaymentMethod(PaymentMethod.resolveMethodById(resultSet.getInt("payment_method_id")))
                    .withOrderStatus(OrderStatus.resolveStatusById(resultSet.getInt("status_id")))
                    .withUser(retrieveUserById(resultSet.getInt("user_id")))
                    .withProducts(ProductDao.INSTANCE.findProductsInOrder(resultSet.getInt("id")))
                    .build();
        } catch (EntityNotFoundException e) {
            LOGGER.error("Failed to parse order result set");
            return Optional.empty();
        }
        return Optional.of(order);
    }

    @Override
    public List<Order> findByField(String searchableField, EntityField<Order> nameOfField) throws DaoException {
        List<Order> orderList = new ArrayList<>();
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveSqlByOrderField((OrderField) nameOfField))) {
                preparedStatement.setString(1, searchableField);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Optional<Order> productOptional = parseResultSet(resultSet);
                    productOptional.ifPresent(orderList::add);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find order by " + nameOfField.toString().toLowerCase());
        }
        return orderList;
    }

    public void deleteOrderProductByProductId(int productId) throws DaoException {
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_ORDER_PRODUCTS)) {
                preparedStatement.setInt(1, productId);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to delete order product with product id = " + productId);
        }
    }

    private void prepareAllOrderStatements(PreparedStatement preparedStatement, Order order) throws SQLException {
        preparedStatement.setString(1, order.getDeliveryAddress());
        preparedStatement.setBigDecimal(2, order.getCost());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(order.getCreateDate()));
        preparedStatement.setTimestamp(4, Timestamp.valueOf(order.getDeliveryDate()));
        preparedStatement.setInt(5, order.getPaymentMethod().ordinal() + 1);
        preparedStatement.setInt(6, order.getOrderStatus().ordinal() + 1);
        preparedStatement.setInt(7, order.getUser().getId());
    }

    private User retrieveUserById(int id) throws DaoException {
        List<User> users = UserDao.INSTANCE.findByField(String.valueOf(id), UserField.ID);
        if (users.size() < 1) {
            LOGGER.error("Failed to load user from order dao");
            throw new DaoException("Failed to load user from order dao");
        }
        return users.get(0);
    }

    private String retrieveSqlByOrderField(OrderField nameOfField) throws DaoException {
        StringBuilder sql = new StringBuilder(SQL_FIND_ALL);
        switch (nameOfField) {
            case ID:
                sql.append(" WHERE id = ?");
                break;
            case USER_ID:
                sql.append(" WHERE user_id = ?");
                break;
            default:
                throw new DaoException(nameOfField.name().toLowerCase() + " - this field does not exist");
        }
        return sql.toString();
    }
}