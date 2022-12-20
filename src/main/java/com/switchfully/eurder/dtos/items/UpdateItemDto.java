package com.switchfully.eurder.dtos.items;

import com.switchfully.eurder.model.items.StockLevel;

public class UpdateItemDto extends CreateItemDto
{
    public UpdateItemDto(String name, String description, String price, String stock) {
        super(name, description, price, stock);
    }

}
