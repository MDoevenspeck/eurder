package com.switchfully.eurder.repositories;

import com.switchfully.eurder.model.items.Item;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemRepository {

    private final Map<String, Item> items = new HashMap<>();

    public Map<String, Item> getAllItems(){
        return items;
    }

    public Optional<Item> getItemById(String id){
        return Optional.ofNullable(items.get(id));
    }
    public Item saveItem(Item item){
        items.put(item.getId(),item);
        return item;
    }
}
