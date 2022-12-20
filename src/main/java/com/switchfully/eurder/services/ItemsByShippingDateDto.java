package com.switchfully.eurder.services;

import com.switchfully.eurder.model.orders.ItemGroup;
import com.switchfully.eurder.model.users.Address;
import com.switchfully.eurder.model.users.User;

import java.util.List;

public record ItemsByShippingDateDto(String firstName, String lastName, Address address, List<ItemGroup> itemGroups) {

}
