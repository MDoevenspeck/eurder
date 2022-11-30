package com.switchfully.eurder.model.users;

import com.switchfully.eurder.model.users.security.Feature;

import java.util.List;

public enum Role {
    ADMIN(Feature.CREATE_ADMIN, Feature.GET_ALL_USERS_BY_USERTYPE, Feature.GET_ALL_USERS,
            Feature.GET_CUSTOMER_BY_ID, Feature.CREATE_ITEM, Feature.UPDATE_ITEM,
            Feature.GET_ALL_ITEMS_BY_STOCK_LEVEL, Feature.GET_ALL_ITEMS_BY_STOCK_LEVEL,
            Feature.CREATE_ORDER, Feature.GET_ALL_ORDERS_BY_CUSTOMER_ID),

    CUSTOMER(Feature.CREATE_ORDER, Feature.GET_ALL_ORDERS_BY_CUSTOMER_ID);

    private final List<Feature> features;

    Role(Feature... features) {
        this.features = List.of(features);
    }

    public boolean hasFeature(Feature feature) {
        return this.features.contains(feature);
    }

}
