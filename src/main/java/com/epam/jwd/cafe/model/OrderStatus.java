package com.epam.jwd.cafe.model;

import com.epam.jwd.cafe.exception.EntityNotFoundException;

/**
 * The class representation {@link Order} status
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public enum OrderStatus {
    ACTIVE,
    CANCELLED,
    COMPLETED,
    UNACCEPTED;

    public static OrderStatus resolveStatusById(int id) throws EntityNotFoundException {
        OrderStatus orderStatus;
        try{
            orderStatus = OrderStatus.values()[id - 1];
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new EntityNotFoundException("Order status with id: " + id + " - not found");
        }
        return orderStatus;
    }
}
