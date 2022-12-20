package com.switchfully.eurder.controllers;

import com.switchfully.eurder.dtos.orders.CreateItemGroupDto;
import com.switchfully.eurder.dtos.orders.OrderDto;
import com.switchfully.eurder.dtos.orders.OrdersDto;
import com.switchfully.eurder.model.orders.ItemGroup;
import com.switchfully.eurder.model.orders.Order;
import com.switchfully.eurder.model.items.Item;
import com.switchfully.eurder.model.items.Price;
import com.switchfully.eurder.model.users.Address;
import com.switchfully.eurder.model.users.Customer;
import com.switchfully.eurder.model.users.Role;
import com.switchfully.eurder.model.users.User;
import com.switchfully.eurder.repositories.ItemGroupRepository;
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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class OrderControllerTest {
    @LocalServerPort
    int port;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemGroupRepository itemGroupRepository;


    @BeforeEach
    public void databaseSetup() {
        Customer Danny = new Customer("Danny", "123", "Danny", "Devito", "dannyD@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
        Customer Jane = new Customer("Jane", "123", "Jane", "Devito", "jane@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
        userRepository.save(Danny);
        userRepository.save(Jane);

        User user = new User("admin", "root", Role.ADMIN);
        userRepository.save(user);


        Item item1 = new Item("Voetbal", "Om te voetballen", new Price(29.5), 15);
        Item item2 = new Item("Basket", "Om te Basketten", new Price(35), 3);
        Item item3 = new Item("Voleybal", "Om te volleyballen", new Price(15), 7);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        Order order1 = new Order(Danny);
        Order order2 = new Order(Jane);

        orderRepository.save(order1);
        orderRepository.save(order2);

        itemGroupRepository.save(new ItemGroup(item1, 5, 29.5, LocalDate.now(), order1.getId()));
        itemGroupRepository.save(new ItemGroup(item2, 1, 35, LocalDate.now(), order1.getId()));
        itemGroupRepository.save(new ItemGroup(item3, 2, 15, LocalDate.now(), order1.getId()));


        itemGroupRepository.save(new ItemGroup(item1, 7, 29.5, LocalDate.now(), order2.getId()));
        itemGroupRepository.save(new ItemGroup(item2, 1, 35, LocalDate.now(), order2.getId()));


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

            assertEquals(orderRepository.findAll().size(), ordersRepo.size());
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
            userRepository.save(matti);

            Item item4 = new Item("Baseball", "Om te baseballen", new Price(7), 20);
            itemRepository.save(item4);

            JSONObject requestParams = new JSONObject();
            requestParams.put("items", List.of(new CreateItemGroupDto(item4.getId(), 3)));

            OrderDto order = RestAssured.given().port(port).auth().preemptive().basic("Matti", "123").contentType("application/json").body(requestParams)
                    .when().post("/orders")
                    .then().assertThat().statusCode(201).extract().as(new TypeRef<OrderDto>() {
                    });

            Order orderInRepo = orderRepository.findAll().stream().filter(order1 -> order1.getCustomer().getId().equals(matti.getId())).findFirst().orElseThrow();
            assertEquals(orderInRepo.getId(), order.orderID());
        }

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAOrderEndpoint_WhenCreatingAnOrderWithoutBeingAUser_ThenThrowError() {
            User matti = new Customer("Matti", "123", "Danny", "Devito", "dannyD@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
            userRepository.save(matti);

            Item item4 = new Item("Baseball", "Om te baseballen", new Price(7), 20);
            itemRepository.save(item4);

            JSONObject requestParams = new JSONObject();
            requestParams.put("items", List.of(new CreateItemGroupDto(item4.getId(), 3)));

            RestAssured.given().port(port).auth().preemptive().basic("", "123").contentType("application/json").body(requestParams)
                    .when().post("/orders")
                    .then().assertThat().statusCode(403);
        }

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAOrderEndpoint_WhenCreatingAnOrderWithNonExitingItem_ThenThrowError() {
            User matti = new Customer("Matti", "123", "Danny", "Devito", "dannyD@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
            userRepository.save(matti);

            Item item4 = new Item("Baseball", "Om te baseballen", new Price(7), 20);
            itemRepository.save(item4);

            JSONObject requestParams = new JSONObject();
            requestParams.put("items", List.of(new CreateItemGroupDto(-54650L, 3)));

            RestAssured.given().port(port).auth().preemptive().basic("Matti", "123").contentType("application/json").body(requestParams)
                    .when().post("/orders")
                    .then().assertThat().statusCode(404);
        }

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAOrderEndpoint_WhenCreatingAnOrderWithItemOutOfStock_ThenShippingDateIsNowPlus7Days() {
            User matti = new Customer("Matti", "123", "Danny", "Devito", "dannyD@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
            userRepository.save(matti);

            Item item4 = new Item("Baseball", "Om te baseballen", new Price(7), 20);
            itemRepository.save(item4);

            JSONObject requestParams = new JSONObject();
            requestParams.put("items", List.of(new CreateItemGroupDto(item4.getId(), 25)));

            RestAssured.given().port(port).auth().preemptive().basic("Matti", "123").contentType("application/json").body(requestParams)
                    .when().post("/orders")
                    .then().assertThat().statusCode(201);

            Order orderInRepo = orderRepository.findAll().stream().filter(order -> order.getCustomer().getId().equals(matti.getId())).findFirst().orElseThrow();
            List<ItemGroup> itemGroups = itemGroupRepository.findAll().stream().filter(itemGroup -> itemGroup.getOrderId() == orderInRepo.getId()).toList();

            assertEquals(LocalDate.now().plusDays(7), itemGroups.get(0).getShippingDate());
        }

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAOrderEndpoint_WhenCreatingAnOrderWithItemInStock_ThenShippingDateIsNowPlus1Days() {
            User matti = new Customer("Matti", "123", "Danny", "Devito", "dannyD@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
            userRepository.save(matti);

            Item item4 = new Item("Baseball", "Om te baseballen", new Price(7), 20);
            itemRepository.save(item4);

            JSONObject requestParams = new JSONObject();
            requestParams.put("items", List.of(new CreateItemGroupDto(item4.getId(), 5)));

            RestAssured.given().port(port).auth().preemptive().basic("Matti", "123").contentType("application/json").body(requestParams)
                    .when().post("/orders")
                    .then().assertThat().statusCode(201);

            Order orderInRepo = orderRepository.findAll().stream().filter(order1 -> order1.getCustomer().getId().equals(matti.getId())).findFirst().orElseThrow();
            List<ItemGroup> itemGroups = itemGroupRepository.findAll().stream().filter(itemGroup -> itemGroup.getOrderId() == orderInRepo.getId()).toList();

            assertEquals(LocalDate.now().plusDays(1), itemGroups.get(0).getShippingDate());
        }
    }
}
