package com.switchfully.eurder.services;

import com.switchfully.eurder.dtos.orders.*;
import com.switchfully.eurder.mappers.OrderMapper;
import com.switchfully.eurder.model.orders.ItemGroup;
import com.switchfully.eurder.model.orders.Order;
import com.switchfully.eurder.model.items.Item;
import com.switchfully.eurder.repositories.ItemRepository;
import com.switchfully.eurder.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {
    public static final int SHIPPING_DAYS_WHEN_NOT_IN_STOCK = 7;
    public static final int SHIPPING_DAYS_WHEN_IN_STOCK = 1;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.getAllOrders().stream().map(this::getOrder).toList();
    }

    private OrderDto getOrder(Order order) {
        List<ItemGroupDto> itemGroupDtos = getItemGroupDtos(order.getItemGroups());
        double orderTotal = itemGroupDtos.stream().mapToDouble(ItemGroupDto::total).sum();
        return orderMapper.toDto(order, itemGroupDtos, orderTotal);
    }

    private List<ItemGroupDto> getItemGroupDtos(List<ItemGroup> itemGroups) {
        return itemGroups.stream().map(itemGroup -> orderMapper.toDto(itemGroup,itemGroup.orderAmount() * itemGroup.itemPriceFrozen())).toList();
    }

    public OrderDto createOrder(CreateOrderDto createOrderDto, String userId) {
        validateItems(createOrderDto.items());
        List<ItemGroup> items = getItemGroups(createOrderDto);
        Order order = orderRepository.saveOrder(orderMapper.toOrder(userId, items));
        return getOrder(order);
    }

    private List<ItemGroup> getItemGroups(CreateOrderDto createOrderDto) {
        List<ItemGroup> items = new ArrayList<>();

        for (CreateItemGroupDto createItemGroupDto : createOrderDto.items()) {
            Item item = itemRepository.getItemById(createItemGroupDto.getItemId()).orElseThrow();
            LocalDate shippingDate = setShippingDate(createItemGroupDto.getAmount(), item.getStock());
            items.add(orderMapper.toItemGroup(createItemGroupDto, item.getPrice().getAmount(), shippingDate));
            updateStock(item, createItemGroupDto.getAmount());
        }
        return items;
    }

    private void validateItems(List<CreateItemGroupDto> items) {
        items.stream().map(CreateItemGroupDto::getItemId).map(itemRepository::getItemById).findFirst().orElseThrow(() -> new NoSuchElementException("Invalid Item id"));
    }

    private void updateStock(Item item, int amount) {
        item.setStock(item.getStock()-amount);
    }

    private static LocalDate setShippingDate(int orderAmount, int stock) {
        if (orderAmount > stock)
            return LocalDate.now().plusDays(SHIPPING_DAYS_WHEN_NOT_IN_STOCK);
        return LocalDate.now().plusDays(SHIPPING_DAYS_WHEN_IN_STOCK);
    }

    public OrdersDto getAllOrdersByCustomerId(String customerId) {
        List<OrderDto> ordersDto = getAllOrders().stream().filter(orderDto -> orderDto.customerId().equals(customerId)).toList();
        double total = ordersDto.stream().mapToDouble(OrderDto::total).sum();
        return new OrdersDto(ordersDto, total);
    }
}
