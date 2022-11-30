package com.switchfully.eurder.mappers;

import com.switchfully.eurder.dtos.items.CreateItemDto;
import com.switchfully.eurder.dtos.items.ItemDto;
import com.switchfully.eurder.model.items.Item;
import com.switchfully.eurder.model.items.Price;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper {
    public List<ItemDto> toDto(List<Item> items, String stockLevel){
        return items.stream().map(item -> toDto(item, stockLevel)).toList();
    }

    public ItemDto toDto(Item item, String stockLevel){
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getPrice().getAmount() +" "+ item.getPrice().getCurrency(), item.getStock(), stockLevel);
    }

    public Item toItem(CreateItemDto createItemDto){
        return new Item(createItemDto.name(), createItemDto.description(), new Price(createItemDto.price()), createItemDto.stock());
    }
}
