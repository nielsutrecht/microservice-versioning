package com.nibado.example.microserviceversioning.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nibado.example.microserviceversioning.model.serializers.OrderSerializer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.valueOf;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

/**
 * This is our Order POJO. It's a simply web shop order object that contains order and item information as well
 * as a bit of logic to include the total in the result.
 */
@JsonSerialize(using = OrderSerializer.class) //Our own serializer that's View-aware.
public class Order {
    private final int id;
    private final String customerFirstName;
    private final String customerLastName;
    private final long timestamp;
    private final List<Item> items;

    public Order(int id, String customerFirstName, String customerLastName, long timestamp, Item... items) {
        this.id = id;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.timestamp = timestamp;
        this.items = asList(items);
    }

    public Order(Order other) {
        this.id = other.id;
        this.customerFirstName = other.customerFirstName;
        this.customerLastName = other.customerLastName;
        this.timestamp = other.timestamp;
        this.items = new ArrayList<>(other.items);
    }

    public int getId() {
        return id;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public BigDecimal getOrderTotal() {
        return items.stream()
                .map(i -> i.price.multiply(valueOf(i.qtty)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Item> getItems() {
        return unmodifiableList(items);
    }

    public static class Item {
        private final String name;
        private final int qtty;
        private final BigDecimal price;

        public Item(String name, int qtty, String price) {
            this.name = name;
            this.qtty = qtty;
            this.price = new BigDecimal(price);
        }

        public Item(Item other) {
            this.name = other.name;
            this.qtty = other.qtty;
            this.price = other.price;
        }

        @JsonView(Object.class)
        public String getName() {
            return name;
        }

        public int getQtty() {
            return qtty;
        }

        public BigDecimal getPrice() {
            return price;
        }
    }
}
