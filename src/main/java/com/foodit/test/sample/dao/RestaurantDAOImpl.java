package com.foodit.test.sample.dao;

import com.foodit.test.sample.model.RestaurantData;
import com.googlecode.objectify.Key;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * @author James Faulkner
 */
public class RestaurantDAOImpl implements RestaurantDAO {

    @Override
    public RestaurantData get(final String restaurantName) {
        return ofy().load().key(Key.create(RestaurantData.class, restaurantName)).now();
    }

    @Override
    public void save(RestaurantData restaurantData) {
        ofy().save().entities(restaurantData);
    }
}
