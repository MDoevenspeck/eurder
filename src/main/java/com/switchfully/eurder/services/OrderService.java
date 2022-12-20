package com.switchfully.eurder.services;

import com.switchfully.eurder.dtos.orders.*;
import com.switchfully.eurder.mappers.OrderMapper;
import com.switchfully.eurder.model.items.Item;
import com.switchfully.eurder.model.orders.ItemGroup;
import com.switchfully.eurder.model.orders.Order;
import com.switchfully.eurder.model.users.Customer;
import com.switchfully.eurder.repositories.ItemGroupRepository;
import com.switchfully.eurder.repositories.ItemRepository;
import com.switchfully.eurder.repositories.OrderRepository;
import com.switchfully.eurder.repositories.UserRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Transactional
@Service
public class OrderService {
    public static final int SHIPPING_DAYS_WHEN_NOT_IN_STOCK = 7;
    public static final int SHIPPING_DAYS_WHEN_IN_STOCK = 1;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final ItemGroupRepository itemGroupRepository;

    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository, UserRepository userRepository, OrderMapper orderMapper, ItemGroupRepository itemGroupRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
        this.itemGroupRepository = itemGroupRepository;
    }

    public List<OrderDto> getAllOrdersWithTotal() {

        return orderRepository.findAll().stream().map(this::getOrderWithTotal).toList();
    }

    private OrderDto getOrderWithTotal(Order order) {

        order.setTotal(itemGroupRepository.findAll().stream().filter(itemGroup -> itemGroup.getOrderId() == order.getId())
                .mapToDouble(ItemGroup -> ItemGroup.getItemPriceFrozen() * ItemGroup.getOrderAmount())
                .sum());
        return orderMapper.toDto(order);
    }

    public OrderDto createOrder(CreateOrderDto createOrderDto, long userId) {
        Order order = new Order();
        Customer customer = userRepository.getCustomerByUserID(userId).orElseThrow();
        order.setCustomer(customer);
        orderRepository.save(order);
        createItemGroups(createOrderDto.items(), order);
        return getOrderWithTotal(order);
    }


    public void createItemGroups(List<CreateItemGroupDto> createItemGroupDtos, Order order) {
        for (CreateItemGroupDto createItemGroupDto : createItemGroupDtos) {
            Item item = itemRepository.findById(createItemGroupDto.itemId()).orElseThrow(() -> new NoSuchElementException("Invalid Item id"));
            LocalDate shippingDate = setShippingDate(createItemGroupDto.amount(), item.getStock());
            int amount = createItemGroupDto.amount();
            updateStock(item, createItemGroupDto.amount());
            ItemGroup itemGroup = new ItemGroup(item, amount, item.getPrice().getAmount(), shippingDate, order.getId());
            itemGroupRepository.save(itemGroup);
            order.getItemGroups().add(itemGroup);
        }
    }

    private static LocalDate setShippingDate(int orderAmount, int stock) {

        if (orderAmount > stock)
            return LocalDate.now().plusDays(SHIPPING_DAYS_WHEN_NOT_IN_STOCK);
        return LocalDate.now().plusDays(SHIPPING_DAYS_WHEN_IN_STOCK);
    }

    private void updateStock(Item item, int amount) {
        item.setStock(item.getStock() - amount);
    }

    public OrdersDto getAllOrdersByCustomerId(long customerId) {
        List<OrderDto> orderDtos = getAllOrdersWithTotal().stream().filter(orderDto -> orderDto.customerId() == customerId).toList();
        double total = orderDtos.stream().mapToDouble(OrderDto::total).sum();
        return new OrdersDto(orderDtos, total);
    }

    public OrderDto reorderById(long userId, CreateReorderDto createReorderDto) {

        Order reorder = orderRepository.findAll().stream()
                .filter(order -> ((order.getId() == createReorderDto.orderId()) && (order.getCustomer().getId() == userId)))
                .findFirst().orElseThrow(() -> new NoSuchElementException("No Existing order by given order id"));

        List<CreateItemGroupDto> createItemGroupDtos = reorder.getItemGroups().stream().map(orderMapper::toCreateItemGroup).toList();

        CreateOrderDto createorderDto = new CreateOrderDto(createItemGroupDtos);
        return createOrder(createorderDto, userId);
    }

    public List<ItemsByShippingDateDto> getItemsByShippingDate() {

        List<Order> orders = orderRepository.findAll();
        Map<Customer, List<ItemGroup>> shippingList = new HashMap<>();

        for (Order order : orders) {
            for (ItemGroup itemGroup : order.getItemGroups()) {
                if (itemGroup.getShippingDate().equals(LocalDate.now().plusDays(1))) {
                    shippingList.computeIfAbsent((Customer) userRepository.findById(order.getCustomer().getId()).orElseThrow(), k -> new ArrayList<>()).add(itemGroup);
                }
            }
        }
        List<ItemsByShippingDateDto> shippingDateDtos = new ArrayList<>();
        for (Customer key : shippingList.keySet()) {
            shippingDateDtos.add(new ItemsByShippingDateDto(key.getFirstname(), key.getLastname(), key.getAddress(), shippingList.get(key)));
        }
        return shippingDateDtos;
    }
}

