package com.epam.jwd.cafe.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class Order extends BaseEntity{
    private final int id;
    private final User user;
    private final PaymentMethod paymentMethod;
    private final BigDecimal cost;
    private final String deliveryAddress;
    private final LocalDateTime createDate;
    private final LocalDateTime deliveryDate;
    private final OrderStatus orderStatus;
    private final Map<Product, Integer> products;

    private Order(int id, User user, PaymentMethod paymentMethod, BigDecimal cost, String deliveryAddress,
                  LocalDateTime createDate, LocalDateTime deliveryDate, OrderStatus orderStatus, Map<Product, Integer> products) {
        this.id = id;
        this.user = user;
        this.paymentMethod = paymentMethod;
        this.cost = cost;
        this.deliveryAddress = deliveryAddress;
        this.createDate = createDate;
        this.deliveryDate = deliveryDate;
        this.orderStatus = orderStatus;
        this.products = products;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", paymentMethod=" + paymentMethod +
                ", cost=" + cost +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", createDate=" + createDate +
                ", deliveryDate=" + deliveryDate +
                ", orderStatus=" + orderStatus +
                ", products=" + products +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(user, order.user) && paymentMethod == order.paymentMethod
                && Objects.equals(cost, order.cost) && Objects.equals(deliveryAddress, order.deliveryAddress)
                && Objects.equals(createDate, order.createDate) && Objects.equals(deliveryDate, order.deliveryDate)
                && orderStatus == order.orderStatus && Objects.equals(products, order.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, paymentMethod, cost, deliveryAddress, createDate, deliveryDate, orderStatus, products);
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private User user;
        private PaymentMethod paymentMethod;
        private BigDecimal cost;
        private String deliveryAddress;
        private LocalDateTime createDate;
        private LocalDateTime deliveryDate;
        private OrderStatus orderStatus;
        private Map<Product, Integer> products;

        private Builder() {
        }

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder withPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder withCost(BigDecimal cost) {
            this.cost = cost;
            return this;
        }

        public Builder withDeliveryAddress(String deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
            return this;
        }

        public Builder withCreateDate(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder withDeliveryDate(LocalDateTime deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        public Builder withOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder withProducts(Map<Product, Integer> products) {
            this.products = products;
            return this;
        }

        public Order build() {
            return new Order(this.id,
                    this.user,
                    this.paymentMethod,
                    this.cost,
                    this.deliveryAddress,
                    this.createDate,
                    this.deliveryDate,
                    this.orderStatus,
                    this.products);
        }
    }

}
