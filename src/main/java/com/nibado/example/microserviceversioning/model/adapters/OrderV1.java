package com.nibado.example.microserviceversioning.model.adapters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nibado.example.microserviceversioning.model.Order;

import java.math.BigDecimal;
import java.util.List;

/**
 * Order adapter that maps the current Order to an older version.
 */
public class OrderV1 {
    private static final BigDecimal HUNDRED = new BigDecimal(100);

    private Order order;

    public OrderV1(Order order) {
        this.order = order;
    }

    public String getCustomerFirstName() {
        return order.getCustomerFirstName();
    }

    @JsonProperty("customerLsatName")
    public String getCustomerLastName() {
        return order.getCustomerLastName();
    }

    public long getOrderTotal() {
        return order.getOrderTotal().multiply(HUNDRED).longValue();
    }

    public long getTimeStamp() {
        return order.getTimestamp();
    }

    public List<Order.Item> getItems() {
        return order.getItems();
    }
}
