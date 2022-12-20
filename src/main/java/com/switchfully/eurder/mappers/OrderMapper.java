package com.switchfully.eurder.mappers;

import com.switchfully.eurder.dtos.orders.CreateItemGroupDto;
import com.switchfully.eurder.dtos.orders.ItemGroupDto;
import com.switchfully.eurder.dtos.orders.OrderDto;
import com.switchfully.eurder.model.orders.ItemGroup;
import com.switchfully.eurder.model.orders.Order;
import com.switchfully.eurder.model.users.Customer;
import com.switchfully.eurder.repositories.ItemRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

@Component
public class OrderMapper {

    private final ItemRepository itemRepository;

    public OrderMapper(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public OrderDto toDto(Order order){
        return new OrderDto(order.getId(), order.getCustomer().getId(), toDto(order.getItemGroups()), order.getTotal());
    }

    public ItemGroupDto toDto(ItemGroup itemGroup, double total){
        return new ItemGroupDto(itemGroup.getItem(), itemGroup.getOrderAmount(), itemGroup.getItemPriceFrozen(), total, itemGroup.getShippingDate());
    }


    public List<ItemGroupDto> toDto(List<ItemGroup> itemGroups){
        return itemGroups.stream().map(itemGroup -> toDto(itemGroup, itemGroup.getOrderAmount()*itemGroup.getItemPriceFrozen())).toList();

    }
//    public ItemGroup toItemGroup(CreateItemGroupDto createItemGroupDto, double itemPrice, LocalDate shippingDate){
//        return new ItemGroup(itemRepository.findById(createItemGroupDto.itemId()).orElseThrow(), createItemGroupDto.amount(), itemPrice, shippingDate, order);
//    }

   public CreateItemGroupDto toCreateItemGroup(ItemGroup itemGroup){
        return new CreateItemGroupDto(itemGroup.getItem().getId(), itemGroup.getOrderAmount());
   }
}
