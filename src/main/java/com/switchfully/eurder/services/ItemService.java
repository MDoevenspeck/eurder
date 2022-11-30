package com.switchfully.eurder.services;

import com.switchfully.eurder.dtos.items.CreateItemDto;
import com.switchfully.eurder.dtos.items.ItemDto;
import com.switchfully.eurder.dtos.items.UpdateItemDto;
import com.switchfully.eurder.mappers.ItemMapper;
import com.switchfully.eurder.model.items.Item;
import com.switchfully.eurder.model.items.Price;
import com.switchfully.eurder.model.items.StockLevel;
import com.switchfully.eurder.repositories.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
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

    public ItemDto createItem(CreateItemDto createItemDto) {
        Item item = itemRepository.saveItem(itemMapper.toItem(createItemDto));
        return itemMapper.toDto(item, calculateStockLevel(item));
    }

    public List<ItemDto> getAllItems() {
        return itemRepository.getAllItems().values().stream().map(item -> itemMapper.toDto(item, calculateStockLevel(item))).toList();
    }

    public List<ItemDto> getAllItemsSortedByStockLevel() {
        return itemRepository.getAllItems().values().stream().sorted(Comparator.comparingInt(Item::getStock)).map(item -> itemMapper.toDto(item, calculateStockLevel(item))).toList();
    }

    public List<ItemDto> getAllItemsByStockLevel(String stockLevel) {
        if (!Arrays.asList(StockLevel.values()).toString().contains(stockLevel)) throw new IllegalArgumentException("invalid stock level");
        List<ItemDto> items = getAllItemsSortedByStockLevel().stream().filter(itemDto -> itemDto.StockLevel().equals(stockLevel)).toList();
        if (items.isEmpty()) throw new NoSuchElementException("no items matching requested stock level where found");
        return items;
    }

    public ItemDto updateItem(UpdateItemDto updateItemDto, String id) {
        Item updateItem = itemRepository.getItemById(id).orElseThrow(() -> new NoSuchElementException("No item with id:" + id + "in our collection"));
        updateItem.setName(updateItemDto.name());
        updateItem.setDescription(updateItem.getDescription());
        updateItem.setPrice(new Price(updateItemDto.price()));
        updateItem.setStock(updateItemDto.stock());
        return itemMapper.toDto(updateItem, calculateStockLevel(updateItem));
    }

    public String calculateStockLevel(Item item) {
        if (item.getStock() < StockLevel.STOCK_LOW.getLevel()) return StockLevel.STOCK_LOW.toString();
        if (item.getStock() < StockLevel.STOCK_MEDIUM.getLevel()) return StockLevel.STOCK_MEDIUM.toString();
        else return StockLevel.STOCK_HIGH.toString();
    }
}
