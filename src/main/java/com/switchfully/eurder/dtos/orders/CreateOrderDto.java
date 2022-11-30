package com.switchfully.eurder.dtos.orders;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public record CreateOrderDto(@Valid @NotNull List<CreateItemGroupDto> items) {
}
