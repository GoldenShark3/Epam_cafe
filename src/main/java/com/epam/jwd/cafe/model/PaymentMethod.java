package com.epam.jwd.cafe.model;

import com.epam.jwd.cafe.exception.EntityNotFoundException;

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
