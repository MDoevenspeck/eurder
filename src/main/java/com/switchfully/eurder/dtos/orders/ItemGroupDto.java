package com.switchfully.eurder.dtos.orders;

import java.time.LocalDate;

public record ItemGroupDto (String itemId, int amount, double itemPriceFrozen, double total, LocalDate shippingDate){
}
