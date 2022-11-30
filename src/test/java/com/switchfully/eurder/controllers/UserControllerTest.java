package com.switchfully.eurder.controllers;

import com.switchfully.eurder.dtos.users.CustomerDto;
import com.switchfully.eurder.dtos.users.UserDto;
import com.switchfully.eurder.model.users.Address;
import com.switchfully.eurder.model.users.Customer;
import com.switchfully.eurder.model.users.User;
import com.switchfully.eurder.repositories.UserRepository;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void databaseSetup() {
        User Danny = new Customer("Danny", "123", "Danny", "Devito", "dannyD@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
        User Jane = new Customer("Jane", "123", "Jane", "Devito", "jane@gmail.com", new Address("Langelostraat", "81", "Lubbeek", "3212"), "016205070");
        userRepository.saveUser(Danny);
        userRepository.saveUser(Jane);
    }

    @DisplayName("Tests regarding get all users")
    @Nested
    class testsRegardingGetAllUsers {

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAUsersEndpoint_WhenRequestingAllUsers_ThenAListOfAllUsersIsReturned() {
            List<UserDto> usersInRepo = RestAssured.given().port(port).auth().preemptive().basic("admin", "root").log().all().contentType("application/json")
                    .when().get("/users").then().statusCode(200).extract().as(new TypeRef<List<UserDto>>() {
                    });
            assertEquals(userRepository.getAllUsers().size(), usersInRepo.size());
        }

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAUsersEndpoint_WhenRequestingAllUsersButNotAuthorized_ThenANotAuthorizedErrorIsThrown() {
            Map<String, String> response = RestAssured.given().port(port).auth().preemptive().basic("Danny", "123")
                    .when().get("/users").then().statusCode(403).extract().body().as(new TypeRef<Map<String, String>>() {
                    });

            String ResponseMessage = new JSONObject(response).get("message").toString();
            assertEquals("Unauthorized", ResponseMessage);
        }
    }

    @DisplayName("Tests regarding getting users by type")
    @Nested
    class gettingUsersByType {
        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAUsersEndpoint_WhenGettingCustomers_ThenGetAllCustomers() {
            List<CustomerDto> usersInRepo = RestAssured.given().port(port).auth().preemptive().basic("admin", "root")
                    .when().get("/users?userType=customer").then().statusCode(200).extract().as(new TypeRef<List<CustomerDto>>() {
                    });
            System.out.println(usersInRepo.get(1).getFirstname());
            assertEquals(2, usersInRepo.size());
        }

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAUsersEndpoint_WhenGettingAdmins_ThenGetAllAdmins() {
            List<CustomerDto> usersInRepo = RestAssured.given().port(port).auth().preemptive().basic("admin", "root")
                    .when().get("/users?userType=admin").then().statusCode(200).extract().as(new TypeRef<List<CustomerDto>>() {
                    });

            assertEquals(1, usersInRepo.size());
        }

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAUsersEndpoint_WhenGettingNonExistingUserType_ThenGetErrorUserTypeNotFound() {
            Map<String, String> response = RestAssured.given().port(port).auth().preemptive().basic("admin", "root")
                    .when().get("/users?userType=456").then().statusCode(400).extract().body().as(new TypeRef<Map<String, String>>() {
                    });
            String ResponseMessage = new JSONObject(response).get("message").toString();

            assertEquals("usertype not found", ResponseMessage);
        }

        @Test
        @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
        void GivenAUsersEndpoint_WhenGettingAdminsWhenNotAdminUser_ThenAccesForbidden() {
            RestAssured.given().port(port).auth().preemptive().basic("Danny", "123")
                    .when().get("/users?userType=admin").then().assertThat().statusCode(403);
        }
    }
}

//    @Test
//    void getCustomerById() {
//        // GIVEN
//
//        // WHEN
//
//        // THEN
//    }
//
//    @Test
//    void createUser() {
//        // GIVEN
//
//        // WHEN
//
//        // THEN
//    }
//
//    @Test
//    void testCreateUser() {
//        // GIVEN
//
//        // WHEN
//
//        // THEN
//    }

