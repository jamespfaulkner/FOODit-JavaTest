package com.foodit.test.sample.dao;

import com.foodit.test.sample.model.RestaurantData;

/**
 * @author James Faulkner
 */
public interface RestaurantDAO {

    public RestaurantData get(String restaurantName);

    public void save(RestaurantData restaurantData);
}
