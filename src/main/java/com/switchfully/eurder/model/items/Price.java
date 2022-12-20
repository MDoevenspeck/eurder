package com.switchfully.eurder.model.items;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    public static final String CURRENCY = "EUR";
    @Column(name= "price_amount")
    private double amount;
    @Column(name = "currency")
    private String currency;

    public Price() {
    }

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
