package com.switchfully.eurder.mappers;

import com.switchfully.eurder.dtos.items.CreateItemDto;
import com.switchfully.eurder.dtos.items.ItemDto;
import com.switchfully.eurder.model.items.Item;
import com.switchfully.eurder.model.items.Price;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper {
    public List<ItemDto> toDto(List<Item> items){
        return items.stream().map(this::toDto).toList();
    }

    public ItemDto toDto(Item item){
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getPrice().getAmount() +" "+ item.getPrice().getCurrency(), item.getStock());
    }

    public Item toItem(CreateItemDto createItemDto){
        return new Item(createItemDto.name(), createItemDto.description(), new Price(createItemDto.price()), createItemDto.stock());
    }
}
