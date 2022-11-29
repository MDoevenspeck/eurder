package com.switchfully.eurder.model.users;

import java.util.List;

public enum Role {
    ADMIN(Feature.CREATE_ADMIN, Feature.GET_ALL_CUSTOMERS, Feature.GET_ALL_USERS, Feature.GET_CUSTOMER_BY_ID,
            Feature.CREATE_ITEM, Feature.UPDATE_ITEM), CUSTOMER;

    private final List<Feature> features;

    Role(Feature... features) {
        this.features = List.of(features);
    }

    public  boolean hasFeature(Feature feature){
        return this.features.contains(feature);
    }

}
