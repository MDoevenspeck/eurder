package com.switchfully.eurder.mappers;

import com.switchfully.eurder.dtos.orders.CreateItemGroupDto;
import com.switchfully.eurder.dtos.orders.ItemGroupDto;
import com.switchfully.eurder.dtos.orders.OrderDto;
import com.switchfully.eurder.model.ItemGroup;
import com.switchfully.eurder.model.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order, List<ItemGroupDto> itemGroupDtos, double total){
        return new OrderDto(order.getOrderId(), order.getCustomerId(), itemGroupDtos, total);
    }

    public ItemGroupDto toDto(ItemGroup itemGroup, double total){
        return new ItemGroupDto(itemGroup.getItemId(), itemGroup.getOrderAmount(), itemGroup.getItemPriceFrozen(), total, itemGroup.getShippingDate());
    }

    public Order toOrder(String customerId, List<ItemGroup> items){

        return new Order(customerId, items);
    }

    public ItemGroup toItemGroup(CreateItemGroupDto createItemGroupDto, double itemPrice, LocalDate shippingDate){
        return new ItemGroup(createItemGroupDto.getItemId(), createItemGroupDto.getAmount(), itemPrice, shippingDate);
    }
}
