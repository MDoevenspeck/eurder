package com.switchfully.eurder.repositories;

import com.switchfully.eurder.model.ItemGroup;
import com.switchfully.eurder.model.Order;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
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
        return orders.put(order.getOrderId(), order);
    }
}
