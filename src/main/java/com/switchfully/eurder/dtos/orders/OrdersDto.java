package com.switchfully.eurder.dtos.orders;

import java.util.List;
import java.util.Objects;

public final class OrdersDto {
    private final List<OrderDto> orders;
    private double totalOffAllOrders;

    public OrdersDto(List<OrderDto> orders, double totalOffAllOrders) {
        this.orders = orders;
        this.totalOffAllOrders = totalOffAllOrders;
    }

    public List<OrderDto> orders() {
        return orders;
    }

    public double totalOffAllOrders() {
        return totalOffAllOrders;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }

    public double getTotalOffAllOrders() {
        return totalOffAllOrders;
    }

    public void setTotalOffAllOrders(double totalOffAllOrders) {
        this.totalOffAllOrders = totalOffAllOrders;
    }
}
