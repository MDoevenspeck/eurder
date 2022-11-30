package com.switchfully.eurder.dtos.orders;

import java.util.List;

public record OrderDto(String orderID, String customerId, List<ItemGroupDto> items, double total) {
}
