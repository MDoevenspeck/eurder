package com.switchfully.eurder.dtos.orders;

import javax.validation.constraints.NotBlank;

public record CreateReorderDto(@NotBlank Long orderId) {
}
