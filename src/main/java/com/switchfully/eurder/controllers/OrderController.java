package com.switchfully.eurder.controllers;

import com.switchfully.eurder.dtos.orders.CreateOrderDto;
import com.switchfully.eurder.dtos.orders.OrderDto;
import com.switchfully.eurder.model.users.security.Feature;
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
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderDto> getAllOrdersByCustomerId(@RequestHeader(required = false) String authorization) {
        String userId = securityService.validateAuthorisation(authorization, Feature.GET_ALL_ORDERS_BY_CUSTOMER_ID);
        return orderService.getAllOrdersByCustomerId(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void createOrder(@Valid @RequestBody CreateOrderDto createOrderDto, @RequestHeader(required = false) String authorization) {
        String userId = securityService.validateAuthorisation(authorization, Feature.CREATE_ORDER);
        orderService.createOrder(createOrderDto, userId);
    }
}