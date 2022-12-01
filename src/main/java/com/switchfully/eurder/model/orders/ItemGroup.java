package com.switchfully.eurder.model.orders;

import java.time.LocalDate;

public record ItemGroup(String itemId, int orderAmount, double itemPriceFrozen, LocalDate shippingDate) {
}
