package com.switchfully.eurder.dtos.orders;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public record CreateItemGroupDto(
        @NotNull
        Long itemId,
        @NotNull @PositiveOrZero @Digits(integer = 10, fraction = 0, message = "Stock amount must be an integer")
        int amount
) {}

