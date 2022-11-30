package com.switchfully.eurder.model.items;


public class Price {

    public static final String CURRENCY = "EUR";
    private final double amount;
    private final String currency;

    public Price(double amount) {
        this.amount = amount;
        this.currency = CURRENCY;
    }
    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
