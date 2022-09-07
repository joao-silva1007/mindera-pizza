package com.mindera.pizza.exceptions;

import com.mindera.pizza.domain.order.OrderStatus;
import com.mindera.pizza.utils.Errors;

public class InvalidStatusChangeException extends IllegalArgumentException{
    public InvalidStatusChangeException(Errors error, OrderStatus currentStatus, OrderStatus attemptedStatus) {
        super(String.format(error.toString(), currentStatus, attemptedStatus));
    }

    public InvalidStatusChangeException(Errors error, OrderStatus attemptedStatus) {
        super(String.format(error.toString(), attemptedStatus));
    }
}
