package com.switchfully.eurder.services;

import com.switchfully.eurder.dtos.items.CreateItemDto;
import com.switchfully.eurder.dtos.items.ItemDto;
import com.switchfully.eurder.dtos.items.UpdateItemDto;
import com.switchfully.eurder.mappers.ItemMapper;
import com.switchfully.eurder.model.items.Item;
import com.switchfully.eurder.model.items.Price;
import com.switchfully.eurder.model.items.StockLevel;
import com.switchfully.eurder.repositories.ItemGroupRepository;
import com.switchfully.eurder.repositories.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ItemGroupRepository itemGroupRepository;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper,
                       ItemGroupRepository itemGroupRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.itemGroupRepository = itemGroupRepository;
    }

    public ItemDto createItem(CreateItemDto createItemDto) {
        Item item = itemRepository.save(itemMapper.toItem(createItemDto));
        item.setStockLevel(calculateStockLevel(item.getStock()));
        return itemMapper.toDto(item);
    }

    public List<ItemDto> getAllItems() {
        return getAllItemsWIthStockLevel().stream().map(itemMapper::toDto).toList();
    }

    public List<Item> getAllItemsWIthStockLevel(){
        List<Item> items = itemRepository.findAll();
        for (Item item : items) {
            item.setStockLevel(calculateStockLevel(item.getStock()));
        }
        return items;
    }
    public List<ItemDto> getAllItemsSortedByStockLevel() {
        return getAllItemsWIthStockLevel().stream().sorted(Comparator.comparingInt(Item::getStock)).map(itemMapper::toDto).toList();
    }

    public List<ItemDto> getAllItemsByStockLevel(String stockLevel) {
        if (!Arrays.asList(StockLevel.values()).toString().contains(stockLevel)) throw new IllegalArgumentException("invalid stock level");
        List<Item> items = getAllItemsWIthStockLevel().stream().filter(item -> item.getStockLevel().toString().equals(stockLevel)).toList();
        if (items.isEmpty()) throw new NoSuchElementException("no items matching requested stock level where found");
        return items.stream().map(itemMapper::toDto).toList();
    }

    public ItemDto updateItem(UpdateItemDto updateItemDto, Long id) {
        Item updateItem = itemRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No item with id:" + id + "in our collection"));
        updateItem.setName(updateItemDto.name());
        updateItem.setDescription(updateItem.getDescription());
        updateItem.setPrice(new Price(updateItemDto.price()));
        updateItem.setStock(updateItemDto.stock());
        updateItem.setStockLevel(calculateStockLevel(updateItem.getStock()));
        return itemMapper.toDto(updateItem);
    }

    public StockLevel calculateStockLevel(int stock) {
        if (stock < StockLevel.STOCK_LOW.getLevel()) return StockLevel.STOCK_LOW;
        if (stock < StockLevel.STOCK_MEDIUM.getLevel()) return StockLevel.STOCK_MEDIUM;
        else return StockLevel.STOCK_HIGH;
    }
}
