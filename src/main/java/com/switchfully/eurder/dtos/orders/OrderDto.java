package com.switchfully.eurder.dtos.orders;

import com.switchfully.eurder.model.orders.ItemGroup;

import java.util.List;

public record OrderDto(long orderID, long customerId, List<ItemGroupDto> items, double total) {
}
