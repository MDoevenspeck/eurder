package com.switchfully.eurder.dtos.orders;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public final class CreateItemGroupDto {
        private final @NotBlank String itemId;
        private final @NotBlank @PositiveOrZero @Digits(integer = 10, fraction = 0, message = "Stock amount must be an integer")
        String StringAmount;

        public CreateItemGroupDto(String itemId, String amount) {
                this.itemId = itemId;
                this.StringAmount = amount;
        }

        public String getItemId() {
                return itemId;
        }

        public String getStringAmount() {
                return StringAmount;
        }

        public int getAmount() {
                return Integer.parseInt(StringAmount);
        }
}
