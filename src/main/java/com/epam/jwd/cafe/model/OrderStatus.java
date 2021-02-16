package com.epam.jwd.cafe.model;

import com.epam.jwd.cafe.exception.EntityNotFoundException;

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
