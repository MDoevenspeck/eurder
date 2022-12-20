package com.switchfully.eurder.controllers;

import com.switchfully.eurder.dtos.orders.*;
import com.switchfully.eurder.model.users.security.Feature;
import com.switchfully.eurder.services.ItemsByShippingDateDto;
import com.switchfully.eurder.services.OrderService;
import com.switchfully.eurder.services.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final SecurityService securityService;

    public OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @GetMapping
    public List<OrderDto> getAllOrders(@RequestHeader(required = false) String authorization) {
        long userId = securityService.validateAuthorisation(authorization, Feature.GET_ALL_ORDERS);
        return orderService.getAllOrdersWithTotal();
    }

    @GetMapping(params = "user", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrdersDto getAllOrdersByCustomerId(@RequestHeader(required = false) String authorization) {
        long userId = securityService.validateAuthorisation(authorization, Feature.GET_ALL_ORDERS_BY_CUSTOMER_ID);
        return orderService.getAllOrdersByCustomerId(userId);
    }

    @GetMapping(params = "shipping_today", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ItemsByShippingDateDto> getAllOrdersShipping_today(@RequestHeader(required = false) String authorization) {
        long userId = securityService.validateAuthorisation(authorization, Feature.GET_ALL_ORDERS_BY_CUSTOMER_ID);
        return orderService.getItemsByShippingDate();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto createOrder(@Valid @RequestBody CreateOrderDto createOrderDto, @RequestHeader(required = false) String authorization) {
        long userId = securityService.validateAuthorisation(authorization, Feature.CREATE_ORDER);
        return orderService.createOrder(createOrderDto, userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/reorder", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto reOrderByExistingOrderId(@RequestBody CreateReorderDto createReorder, @RequestHeader(required = false) String authorization) {
        long userId = securityService.validateAuthorisation(authorization, Feature.CREATE_ORDER);
        return orderService.reorderById(userId, createReorder);
    }
}
