package com.foodit.test.sample.mock;

import com.foodit.test.sample.dao.RestaurantDAO;
import com.foodit.test.sample.model.RestaurantData;

/**
* @author James Faulkner
* @since 4.8
*/
public class MockRestaurantDAO implements RestaurantDAO {

    @Override
    public RestaurantData get(final String restaurantName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(final RestaurantData restaurantData) {
        throw new UnsupportedOperationException();
    }
}
