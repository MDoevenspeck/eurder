package com.switchfully.eurder.mappers;

import com.switchfully.eurder.dtos.items.CreateItemDto;
import com.switchfully.eurder.dtos.items.ItemDto;
import com.switchfully.eurder.model.items.Item;
import com.switchfully.eurder.model.items.Price;
import com.switchfully.eurder.model.items.StockLevel;
import com.switchfully.eurder.services.ItemService;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public ItemDto toDto(Item item){
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice().getAmount() +" "+ item.getPrice().getCurrency(),
                item.getStock(),
                item.getStockLevel().toString());
    }

    public Item toItem(CreateItemDto createItemDto){
        return new Item(createItemDto.name(), createItemDto.description(), new Price(createItemDto.price()), createItemDto.stock());
    }
}
