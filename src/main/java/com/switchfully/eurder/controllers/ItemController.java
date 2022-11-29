package com.switchfully.eurder.controllers;

import com.switchfully.eurder.dtos.items.CreateItemDto;
import com.switchfully.eurder.dtos.items.ItemDto;
import com.switchfully.eurder.dtos.items.UpdateItemDto;
import com.switchfully.eurder.model.users.Feature;
import com.switchfully.eurder.services.ItemService;
import com.switchfully.eurder.services.SecurityService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final SecurityService securityService;

    public ItemController(ItemService itemService, SecurityService securityService) {
        this.itemService = itemService;
        this.securityService = securityService;
    }
    @GetMapping
    public List<ItemDto> getAllItems(){
        return itemService.getAllItems();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemDto createItem(@Valid @RequestBody CreateItemDto createItemDto, @RequestHeader(required = false) String authorization){
        securityService.validateAuthorisation(authorization, Feature.CREATE_ITEM);
        return itemService.createItem(createItemDto);
    }
    @PutMapping(path = ("/{id}"), consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemDto updateItem(@PathVariable String id,@Valid @RequestBody UpdateItemDto updateItemDto, @RequestHeader(required = false) String authorization){
        securityService.validateAuthorisation(authorization, Feature.UPDATE_ITEM);
        return itemService.updateItem(updateItemDto, id);
    }
}
