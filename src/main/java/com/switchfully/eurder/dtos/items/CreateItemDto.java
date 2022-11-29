package com.switchfully.eurder.dtos.items;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class CreateItemDto {
    @NotBlank
    private final String name;
    @NotBlank
    private final String description;

    @NotBlank
    @PositiveOrZero
    private final String price;

    @NotBlank
    @PositiveOrZero
    @Digits(integer = 10, fraction = 0, message = "Stock amount must be an integer")
    private final String stock;

    public CreateItemDto(String name, String description, String price, String stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public double price() {
        return Double.parseDouble(price);
    }

    public int stock() {
        return Integer.parseInt(stock);
    }
}
