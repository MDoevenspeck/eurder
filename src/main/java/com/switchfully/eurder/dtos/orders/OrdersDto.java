package com.switchfully.eurder.dtos.orders;

import java.util.List;

public record OrdersDto(List<OrderDto> orders, double totalOffAllOrders) {
}
