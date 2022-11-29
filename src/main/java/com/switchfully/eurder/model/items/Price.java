package com.switchfully.eurder.model.items;


public class Price {

    private final double amount;
    private final String currency;

    public Price(double amount) {
        this.amount = amount;
        this.currency = "EUR";
    }
    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
