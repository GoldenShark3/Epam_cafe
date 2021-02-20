package com.epam.jwd.cafe.dao.impl;

import com.epam.jwd.cafe.dao.AbstractDao;
import com.epam.jwd.cafe.dao.field.EntityField;
import com.epam.jwd.cafe.dao.field.OrderField;
import com.epam.jwd.cafe.dao.field.ReviewField;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.model.Order;
import com.epam.jwd.cafe.model.Review;
import com.epam.jwd.cafe.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewDao extends AbstractDao<Review> {
    public static final ReviewDao INSTANCE = new ReviewDao(ConnectionPool.getInstance());

    private static final String SQL_FIND_ALL = "SELECT id, feedback, rate, order_id FROM review";
    private static final String SQL_CREATE = "INSERT INTO review(feedback, rate, order_id) VALUES(?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE review SET id = ?, feedback = ?, rate = ?, order_id = ? WHERE id = ";
    private static final String SQL_DELETE = "DELETE review WHERE id = ?";

    private ReviewDao(ConnectionPool connectionPool) {
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
    protected void prepareCreateStatement(PreparedStatement preparedStatement, Review entity) throws SQLException {
        preparedStatement.setString(1, entity.getFeedback());
        preparedStatement.setInt(2, entity.getRate());
        preparedStatement.setInt(3, entity.getOrder().getId());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement preparedStatement, Review entity) throws SQLException {
        preparedStatement.setString(1, entity.getFeedback());
        preparedStatement.setInt(2, entity.getRate());
        preparedStatement.setInt(3, entity.getOrder().getId());
        preparedStatement.setInt(4, entity.getId());
    }

    @Override
    protected Optional<Review> parseResultSet(ResultSet resultSet) throws SQLException, DaoException {
        int id = resultSet.getInt("order_id");
        List<Order> orderList = OrderDao.INSTANCE.findByField(String.valueOf(id), OrderField.ID);

        if (!orderList.isEmpty()) {
            Review review = Review.builder()
                    .withId(resultSet.getInt("id"))
                    .withFeedback(resultSet.getString("feedback"))
                    .withRate(resultSet.getInt("rate"))
                    .withOrder(orderList.get(0))
                    .build();
            return Optional.of(review);
        }
        return Optional.empty();
    }

    @Override
    public List<Review> findByField(String searchableField, EntityField<Review> nameOfField) throws DaoException {
        List<Review> reviewList = new ArrayList<>();
        try (Connection connection = connectionPool.retrieveConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveSqlByReviewField((ReviewField) nameOfField))) {
                preparedStatement.setString(1, searchableField);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Optional<Review> reviewOptional = parseResultSet(resultSet);
                    reviewOptional.ifPresent(reviewList::add);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find review by " + nameOfField.toString().toLowerCase());
        }
        return reviewList;
    }

    private String retrieveSqlByReviewField(ReviewField nameOfField) throws DaoException {
        StringBuilder sql = new StringBuilder(SQL_FIND_ALL);
        switch (nameOfField) {
            case ID:
                sql.append(" WHERE id = ?");
                break;
            case RATE:
                sql.append(" WHERE rate = ?");
                break;
            default:
                return sql.toString();
        }
        throw new DaoException(nameOfField.name().toLowerCase() + " - this field does not exist");
    }
}
