package com.switchfully.eurder.repositories;

import com.switchfully.eurder.model.items.Item;
import com.switchfully.eurder.model.items.Price;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemRepository {

    private final Map<String, Item> items = new HashMap<>();

    public ItemRepository() {
        Item test = new Item("test", "testd", new Price(50), 50 );
        items.put(test.getId(), test);
    }

    public List<Item> getAllItems(){
        return items.values().stream().toList();
    }

    public Optional<Item> getItemById(String id){
        return Optional.ofNullable(items.get(id));
    }
    public Item saveItem(Item item){
        items.put(item.getId(),item);
        return item;
    }
}
