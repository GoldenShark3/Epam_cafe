package com.epam.jwd.cafe.service;

import com.epam.jwd.cafe.config.ApplicationConfig;
import com.epam.jwd.cafe.dao.field.OrderField;
import com.epam.jwd.cafe.dao.field.UserField;
import com.epam.jwd.cafe.dao.impl.OrderDao;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.Order;
import com.epam.jwd.cafe.model.OrderStatus;
import com.epam.jwd.cafe.model.PaymentMethod;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.tag.PaginationTag;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private final Logger LOGGER = LogManager.getLogger(OrderService.class);
    public static final OrderService INSTANCE = new OrderService();
    private static final OrderDao ORDER_DAO = OrderDao.INSTANCE;

    private OrderService() {
    }

    public List<Order> findAllOrders() throws ServiceException {
        try {
            return ORDER_DAO.findAll();
        } catch (DaoException e) {
            LOGGER.error("Failed to find all orders");
            throw new ServiceException(e);
        }
    }

    public Optional<String> createOrder(Order order) throws ServiceException {
        int loyaltyPointsPerDollar;
        User orderUser = order.getUser();
        UserService userService = UserService.INSTANCE;

        if (orderUser.getIsBlocked()) {
            return Optional.of("serverMessage.blockedAccount");
        }
        if (orderUser.getBalance().compareTo(BigDecimal.ZERO) <= 0
                && order.getPaymentMethod().equals(PaymentMethod.BALANCE)) {
            return Optional.of("serverMessage.insufficientBalance");
        }

        try {
            ORDER_DAO.create(order);
            loyaltyPointsPerDollar = ApplicationConfig.getInstance().getLoyaltyPointsPerDollar();
        } catch (DaoException | NumberFormatException e) {
            LOGGER.error("Failed to create order");
            throw new ServiceException(e);
        }

        User.Builder userBuilder = receiveUserBuilder(orderUser);
        userBuilder.withLoyaltyPoints(orderUser.getLoyaltyPoints() + loyaltyPointsPerDollar * order.getCost().intValue());
        if (order.getPaymentMethod().equals(PaymentMethod.BALANCE)) {
            userBuilder.withBalance(orderUser.getBalance().subtract(order.getCost()));
        }
        User user = userBuilder.build();
        userService.updateUser(user);

        return Optional.empty();
    }

    public Optional<Order> findOrderById(int orderId) throws ServiceException {
        List<Order> orders;
        try {
            orders = ORDER_DAO.findByField(String.valueOf(orderId), OrderField.ID);
        } catch (DaoException e) {
            LOGGER.error("Failed on a order search");
            throw new ServiceException("Failed search order by id", e);
        }
        return ((orders.size() > 0) ? Optional.of(orders.get(0)) : Optional.empty());
    }

    public List<Order> findAllOrdersByUserId(int userId) throws ServiceException {
        try {
            return ORDER_DAO.findByField(String.valueOf(userId), OrderField.USER_ID);
        } catch (DaoException e) {
            LOGGER.error("Failed to find all orders by user id = " + userId);
            throw new ServiceException(e);
        }
    }

    public void updateOrder(Order order) throws ServiceException {
        try {
            ORDER_DAO.update(order);
            updateUserBalanceAndLoyaltyPoints(order);
        } catch (DaoException e) {
            LOGGER.error("Failed to update order");
            throw new ServiceException(e);
        }
    }

    private void updateUserBalanceAndLoyaltyPoints(Order order) throws ServiceException {
        User.Builder userBuilder = receiveUserBuilder(order.getUser());
        BigDecimal orderCost = order.getCost();
        PaymentMethod paymentMethod = order.getPaymentMethod();
        OrderStatus orderStatus = order.getOrderStatus();
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        int pointsPerDollar = applicationConfig.getLoyaltyPointsPerDollar();

        if (orderStatus.equals(OrderStatus.CANCELLED)) {
            if (paymentMethod.equals(PaymentMethod.BALANCE)) {
                userBuilder.withBalance((order.getUser().getBalance()).add(orderCost));
            }
            userBuilder.withLoyaltyPoints(order.getUser().getLoyaltyPoints() - orderCost.intValue() * pointsPerDollar);
        }

        if (orderStatus.equals(OrderStatus.UNACCEPTED) && paymentMethod.equals(PaymentMethod.BALANCE)) {
            userBuilder.withLoyaltyPoints(order.getUser().getLoyaltyPoints() - orderCost.intValue() * pointsPerDollar - pointsPerDollar);
            if (userBuilder.build().getLoyaltyPoints() < applicationConfig.getLoyaltyPointsToBlock()) {
                userBuilder.withIsBlocked(true);
            }
        }
        UserService.INSTANCE.updateUser(userBuilder.build());
    }

    private User.Builder receiveUserBuilder(User user) {
        return User.builder()
                .withId(user.getId())
                .withEmail(user.getEmail())
                .withUsername(user.getUsername())
                .withRole(user.getRole())
                .withPassword(user.getPassword())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withPhoneNumber(user.getPhoneNumber())
                .withIsBlocked(user.getIsBlocked())
                .withBalance(user.getBalance());
    }

}
