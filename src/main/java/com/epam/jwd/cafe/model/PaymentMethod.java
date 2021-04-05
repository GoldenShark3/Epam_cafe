package com.epam.jwd.cafe.model;

import com.epam.jwd.cafe.exception.EntityNotFoundException;

/**
 * The class representation of {@link Order} payment method
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public enum PaymentMethod {
    BALANCE,
    CARD,
    CASH;

    public static PaymentMethod resolveMethodById(int id) throws EntityNotFoundException {
        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.values()[id - 1];
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new EntityNotFoundException("Payment method with id: " + id + " - not found");
        }
        return paymentMethod;
    }
}
