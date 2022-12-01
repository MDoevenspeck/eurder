package com.switchfully.eurder.model.orders;

import java.util.List;
import java.util.UUID;

public class Order {

    private final String orderId = String.valueOf(UUID.randomUUID());
    private final String customerId;
    private final List<ItemGroup> itemGroups;

    public Order(String customerId, List<ItemGroup> orderedItems) {
        this.customerId = customerId;
        this.itemGroups = orderedItems;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }

}
