package com.switchfully.eurder.model;

import java.time.LocalDate;

public class ItemGroup {
    private final String itemId;
    private final int orderAmount;
    private final double itemPriceFrozen;
    private final LocalDate localDate;
    private final LocalDate shippingDate;

    public ItemGroup(String itemId, int orderAmount, double itemPriceFrozen, LocalDate shippingDate) {
        this.itemId = itemId;
        this.orderAmount = orderAmount;
        this.itemPriceFrozen = itemPriceFrozen;
        this.localDate = LocalDate.now();
        this.shippingDate = shippingDate;
    }

    public String getItemId() {
        return itemId;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public double getItemPriceFrozen() {
        return itemPriceFrozen;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }
}
