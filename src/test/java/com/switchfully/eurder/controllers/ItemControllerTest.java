package com.switchfully.eurder.controllers;

import com.switchfully.eurder.dtos.items.ItemDto;
import com.switchfully.eurder.model.items.Item;
import com.switchfully.eurder.model.items.Price;
import com.switchfully.eurder.repositories.ItemRepository;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    public void databaseSetup() {
        Item item1 = new Item("Voetbal", "Om te voetballen", new Price(29.5), 15);
        Item item2 = new Item("Basket", "Om te Basketten", new Price(35), 3);
        Item item3 = new Item("Voleybal", "Om te volleyballen", new Price(15), 7);

        itemRepository.saveItem(item1);
        itemRepository.saveItem(item2);
        itemRepository.saveItem(item3);
    }

    @DisplayName("Tests regarding getting items")
    @Nested
    class testsRegardingGetAllItems {

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAItemEndpoint_WhenRequestingAllItems_ThenAListOfAllItemsIsReturned() {
            List<ItemDto> itemsInRepo = RestAssured.given().port(port).auth().preemptive().basic("admin", "root").log().all().contentType("application/json")
                    .when().get("/items").then().statusCode(200).extract().as(new TypeRef<List<ItemDto>>() {
                    });

            assertEquals(itemRepository.getAllItems().size(), itemsInRepo.size());
        }
        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAItemEndpoint_WhenRequestingAllItemsSortedByStock_ThenAListOfAllItemsIsReturnedSortedByStock() {

            Item item4 = new Item("Golfball", "Om te golfen", new Price(15), 0);
            itemRepository.saveItem(item4);

            List<ItemDto> itemsInRepo = RestAssured.given().port(port).auth().preemptive().basic("admin", "root")
                    .when().get("/items?sorted_by_stock").then().statusCode(200).extract().as(new TypeRef<List<ItemDto>>() {
                    });

            assertEquals(item4.getId(), itemsInRepo.get(0).id());
        }
        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAItemEndpoint_WhenRequestingAllItemsSortedByStockWhenNotAdmin_TheThrowError() {

            Item item4 = new Item("Golfball", "Om te golfen", new Price(15), 0);
            itemRepository.saveItem(item4);

            RestAssured.given().port(port).auth().preemptive().basic("Johnny", "root")
                    .when().get("/items?sorted_by_stock").then().statusCode(403);
        }
    }
}