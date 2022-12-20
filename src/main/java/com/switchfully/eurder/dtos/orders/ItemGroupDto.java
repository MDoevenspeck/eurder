package com.switchfully.eurder.dtos.orders;

import com.switchfully.eurder.model.items.Item;

import java.time.LocalDate;

public record ItemGroupDto (Item item, int amount, double price, double total, LocalDate shippingDate){
}
