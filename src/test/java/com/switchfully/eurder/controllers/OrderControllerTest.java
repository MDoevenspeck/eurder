package com.switchfully.eurder.controllers;

import com.switchfully.eurder.dtos.items.ItemDto;
import com.switchfully.eurder.dtos.orders.CreateItemGroupDto;
import com.switchfully.eurder.dtos.orders.ItemGroupDto;
import com.switchfully.eurder.dtos.orders.OrderDto;
import com.switchfully.eurder.dtos.orders.OrdersDto;
import com.switchfully.eurder.model.ItemGroup;
import com.switchfully.eurder.model.Order;
import com.switchfully.eurder.model.items.Item;
import com.switchfully.eurder.model.items.Price;
import com.switchfully.eurder.model.users.Address;
import com.switchfully.eurder.model.users.Customer;
import com.switchfully.eurder.model.users.User;
import com.switchfully.eurder.repositories.ItemRepository;
import com.switchfully.eurder.repositories.OrderRepository;
import com.switchfully.eurder.repositories.UserRepository;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {
    @LocalServerPort
    int port;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;


    @BeforeEach
    public void databaseSetup() {
        User Danny = new Customer("Danny", "123", "Danny", "Devito", "dannyD@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
        User Jane = new Customer("Jane", "123", "Jane", "Devito", "jane@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
        userRepository.saveUser(Danny);
        userRepository.saveUser(Jane);

        Item item1 = new Item("Voetbal", "Om te voetballen", new Price(29.5), 15);
        Item item2 = new Item("Basket", "Om te Basketten", new Price(35), 3);
        Item item3 = new Item("Voleybal", "Om te volleyballen", new Price(15), 7);

        itemRepository.saveItem(item1);
        itemRepository.saveItem(item2);
        itemRepository.saveItem(item3);

        List<ItemGroup> orderItems1 = new ArrayList<>();
        orderItems1.add(new ItemGroup(item1.getId(), 5, 29.5, LocalDate.now()));
        orderItems1.add(new ItemGroup(item2.getId(), 1, 35, LocalDate.now()));
        orderItems1.add(new ItemGroup(item3.getId(), 2, 15, LocalDate.now()));

        List<ItemGroup> orderItems2 = new ArrayList<>();
        orderItems2.add(new ItemGroup(item1.getId(), 7, 29.5, LocalDate.now()));
        orderItems2.add(new ItemGroup(item2.getId(), 1, 35, LocalDate.now()));

        Order order1 = new Order(Danny.getId(), orderItems1);
        Order order2 = new Order(Jane.getId(), orderItems2);

        orderRepository.saveOrder(order1);
        orderRepository.saveOrder(order2);
    }

    @DisplayName("Tests regarding getting orders")
    @Nested
    class testsRegardingGettingOrders {

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAOrderEndpoint_WhenRequestingAllOrders_ThenAListOfAllOrdersIsReturned() {
            List<OrderDto> ordersRepo = RestAssured.given().port(port).auth().preemptive().basic("admin", "root").log().all().contentType("application/json")
                    .when().get("/orders").then().statusCode(200).extract().as(new TypeRef<List<OrderDto>>() {
                    });

            assertEquals(orderRepository.getAllOrders().size(), ordersRepo.size());
        }

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAOrderEndpoint_WhenRequestingAllOrdersByNotAuthorizedUser_ThenThrowError() {
            RestAssured.given().port(port).auth().preemptive().basic("", "root").log().all().contentType("application/json")
                    .when().get("/orders").then().statusCode(403);

        }

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAOrderEndpoint_WhenRequestingAllOrdersByAdminWithoutCorrectPass_ThenThrowError() {
            RestAssured.given().port(port).auth().preemptive().basic("admin", "123").log().all().contentType("application/json")
                    .when().get("/orders").then().statusCode(403);
        }

    }

    @DisplayName("Tests regarding getting orders by user id")
    @Nested
    class testsRegardingGettingOrdersByUserId {

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAOrderEndpoint_WhenRequestingAllOrdersByAUser_ThenAListOfAllUserOrdersIsReturned() {
            OrdersDto ordersRepo1 = RestAssured.given().port(port).auth().preemptive().basic("Danny", "123").log().all().contentType("application/json")
                    .when().get("/orders?user").then().statusCode(200).extract().as(new TypeRef<OrdersDto>() {
                    });

            assertEquals(212.5, ordersRepo1.totalOffAllOrders());

            OrdersDto ordersRepo2 = RestAssured.given().port(port).auth().preemptive().basic("admin", "root").contentType("application/json")
                    .when().get("/orders?user").then().statusCode(200).extract().as(new TypeRef<OrdersDto>() {
                    });
            assertEquals(0.0, ordersRepo2.totalOffAllOrders());
        }

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAOrderEndpoint_WhenRequestingAllOrdersByANonExistingUser_ThenAErrorIsThrown() {
            RestAssured.given().port(port).auth().preemptive().basic("Hacker", "").contentType("application/json")
                    .when().get("/orders?user").then().statusCode(403);
        }
    }

    @DisplayName("Tests regarding creating orders")
    @Nested
    class testsRegardingCreatingOrders {

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAOrderEndpoint_WhenCreatingAnOrder_ThenTheOrderIsAddedToTheOrders() {
            User matti = new Customer("Matti", "123", "Danny", "Devito", "dannyD@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
            userRepository.saveUser(matti);

            Item item4 = new Item("Baseball", "Om te baseballen", new Price(7), 20);
            itemRepository.saveItem(item4);

            JSONObject requestParams = new JSONObject();
            requestParams.put("items", List.of(new CreateItemGroupDto(item4.getId(), "3")));

            OrderDto order = RestAssured.given().port(port).auth().preemptive().basic("Matti", "123").contentType("application/json").body(requestParams)
                    .when().post("/orders")
                    .then().assertThat().statusCode(201).extract().as(new TypeRef<OrderDto>() {
                    });

            Order orderInRepo = orderRepository.getAllOrders().stream().filter(order1 -> order1.getCustomerId().equals(matti.getId())).findFirst().orElseThrow();
            assertEquals(orderInRepo.getOrderId(), order.orderID());
        }

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAOrderEndpoint_WhenCreatingAnOrderWithoutBeingAUser_ThenThrowError() {
            User matti = new Customer("Matti", "123", "Danny", "Devito", "dannyD@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
            userRepository.saveUser(matti);

            Item item4 = new Item("Baseball", "Om te baseballen", new Price(7), 20);
            itemRepository.saveItem(item4);

            JSONObject requestParams = new JSONObject();
            requestParams.put("items", List.of(new CreateItemGroupDto(item4.getId(), "3")));

            RestAssured.given().port(port).auth().preemptive().basic("", "123").contentType("application/json").body(requestParams)
                    .when().post("/orders")
                    .then().assertThat().statusCode(403);
        }

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAOrderEndpoint_WhenCreatingAnOrderWithNonExitingItem_ThenThrowError() {
            User matti = new Customer("Matti", "123", "Danny", "Devito", "dannyD@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
            userRepository.saveUser(matti);

            Item item4 = new Item("Baseball", "Om te baseballen", new Price(7), 20);
            itemRepository.saveItem(item4);

            JSONObject requestParams = new JSONObject();
            requestParams.put("items", List.of(new CreateItemGroupDto("non existing id", "3")));

            RestAssured.given().port(port).auth().preemptive().basic("Matti", "123").contentType("application/json").body(requestParams)
                    .when().post("/orders")
                    .then().assertThat().statusCode(404);
        }
        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAOrderEndpoint_WhenCreatingAnOrderWithItemOutOfStock_ThenShippingDateIsNowPlus7Days() {
            User matti = new Customer("Matti", "123", "Danny", "Devito", "dannyD@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
            userRepository.saveUser(matti);

            Item item4 = new Item("Baseball", "Om te baseballen", new Price(7), 20);
            itemRepository.saveItem(item4);

            JSONObject requestParams = new JSONObject();
            requestParams.put("items", List.of(new CreateItemGroupDto(item4.getId(), "25")));

            OrderDto order = RestAssured.given().port(port).auth().preemptive().basic("Matti", "123").contentType("application/json").body(requestParams)
                    .when().post("/orders")
                    .then().assertThat().statusCode(201).extract().as(new TypeRef<OrderDto>() {
                    });

            Order orderInRepo = orderRepository.getAllOrders().stream().filter(order1 -> order1.getCustomerId().equals(matti.getId())).findFirst().orElseThrow();
            assertEquals(LocalDate.now().plusDays(7), orderInRepo.getItemGroups().get(0).getShippingDate());
        }
        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAOrderEndpoint_WhenCreatingAnOrderWithItemOutInStock_ThenShippingDateIsNowPlus1Days() {
            User matti = new Customer("Matti", "123", "Danny", "Devito", "dannyD@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
            userRepository.saveUser(matti);

            Item item4 = new Item("Baseball", "Om te baseballen", new Price(7), 20);
            itemRepository.saveItem(item4);

            JSONObject requestParams = new JSONObject();
            requestParams.put("items", List.of(new CreateItemGroupDto(item4.getId(), "5")));

            OrderDto order = RestAssured.given().port(port).auth().preemptive().basic("Matti", "123").contentType("application/json").body(requestParams)
                    .when().post("/orders")
                    .then().assertThat().statusCode(201).extract().as(new TypeRef<OrderDto>() {
                    });

            Order orderInRepo = orderRepository.getAllOrders().stream().filter(order1 -> order1.getCustomerId().equals(matti.getId())).findFirst().orElseThrow();
            assertEquals(LocalDate.now().plusDays(1), orderInRepo.getItemGroups().get(0).getShippingDate());
        }
    }
}
