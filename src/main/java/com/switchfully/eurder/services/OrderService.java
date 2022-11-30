package com.switchfully.eurder.services;

import com.switchfully.eurder.dtos.orders.CreateItemGroupDto;
import com.switchfully.eurder.dtos.orders.CreateOrderDto;
import com.switchfully.eurder.dtos.orders.ItemGroupDto;
import com.switchfully.eurder.dtos.orders.OrderDto;
import com.switchfully.eurder.mappers.OrderMapper;
import com.switchfully.eurder.model.ItemGroup;
import com.switchfully.eurder.model.Order;
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
        double total = 0;
        List<ItemGroupDto> itemGroupDtos = new ArrayList<>();
        for (ItemGroup itemGroup : order.getItemGroups()) {
            double subtotal = itemGroup.getOrderAmount() * itemGroup.getItemPriceFrozen();
            total += subtotal;
            itemGroupDtos.add(orderMapper.toDto(itemGroup, subtotal));
        }
        return orderMapper.toDto(order, itemGroupDtos, total);
    }

    public void createOrder(CreateOrderDto createOrderDto, String userId) {
        validateItems(createOrderDto.items());
        List<ItemGroup> items = new ArrayList<>();

        for (CreateItemGroupDto createItemGroupDto : createOrderDto.items()) {
            Item item = itemRepository.getItemById(createItemGroupDto.getItemId()).orElseThrow();

            LocalDate shippingDate = setShippingDate(createItemGroupDto.getAmount(), item.getStock());
            items.add(orderMapper.toItemGroup(createItemGroupDto, item.getPrice().getAmount(), shippingDate));

            updateStock(item, createItemGroupDto.getAmount());
        }
        Order order = orderRepository.saveOrder(orderMapper.toOrder(userId, items));
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

    public List<OrderDto> getAllOrdersByCustomerId(String customerId) {
        return getAllOrders().stream().filter(orderDto -> orderDto.customerId().equals(customerId)).toList();
    }
}
