package com.switchfully.eurder.dtos.orders;

import com.switchfully.eurder.model.ItemGroup;

import java.util.List;

public record OrderDto(String orderID, String customerId, List<ItemGroupDto> items, double total) {
}
