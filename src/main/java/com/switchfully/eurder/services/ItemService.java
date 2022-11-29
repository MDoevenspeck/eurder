package com.switchfully.eurder.services;

import com.switchfully.eurder.dtos.items.CreateItemDto;
import com.switchfully.eurder.dtos.items.ItemDto;
import com.switchfully.eurder.dtos.items.UpdateItemDto;
import com.switchfully.eurder.mappers.ItemMapper;
import com.switchfully.eurder.model.items.Item;
import com.switchfully.eurder.model.items.Price;
import com.switchfully.eurder.repositories.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public ItemDto createItem(CreateItemDto createItemDto){
        return itemMapper.toDto(itemRepository.saveItem(itemMapper.toItem(createItemDto)));
    }

    public List<ItemDto> getAllItems(){
        return itemMapper.toDto(itemRepository.getAllItems());
    }

    public ItemDto updateItem(UpdateItemDto updateItemDto, String id) {
        Item updateItem = itemRepository.getItemById(id).orElseThrow(() -> new NoSuchElementException("No item with id:" + id + "in our collection" ));
        updateItem.setName(updateItemDto.name());
        updateItem.setDescription(updateItem.getDescription());
        updateItem.setPrice(new Price(updateItemDto.price()));
        updateItem.setStock(updateItemDto.stock());
        return itemMapper.toDto(updateItem);
    }
}
