package com.switchfully.eurder.repositories;

import com.switchfully.eurder.model.orders.Order;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {
    private final Map<String, Order> orders = new HashMap<>();

    public List<Order> getAllOrders(){
        return orders.values().stream().toList();
    }

    public Order saveOrder(Order order){
        orders.put(order.getOrderId(), order);
        return order;
    }
}
