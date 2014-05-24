package com.foodit.test.sample.service;

import java.util.Collection;

import com.foodit.test.sample.model.RestaurantData;

/**
 * @author James Faulkner
 */
public interface RestaurantDataService {

    /**
     * Get a restaurantData object by its name.
     *
     * @param restaurantName the name of the RestaurantData to get
     * @return the referenced RestaurantData or null if it does not exist
     */
    public RestaurantData get(String restaurantName);

    /**
     * Save a RestaurantData
     *
     * @param restaurantData the object to save
     */
    public void save(RestaurantData restaurantData);

    /**
     * Get all RestaurantData objects
     *
     * @return all RestaurantData objects currently stored
     */
    public Collection<RestaurantData> findAll();

    /**
     * Used to initialise the data
     */
    public void initData();

    /**
     * Get the number of orders that have been created for a Restaurant
     *
     * @param restaurantName the name of the restaurant that the number of orders should be retrieved for
     * @return the number of orders for the restaurant
     */
    public long orderCount(String restaurantName);

    /**
     * Get the total amount of sales for a restaurant
     *
     * @param restaurantName the name of the restaurant that the total number of sales should be retrieved for
     * @return the total amount of sales
     */
    public double salesValue(final String restaurantName);

    /**
     * Get a JSON string of the most popular meals in the entire system
     *
     * @return a JSON String of the most popular meals.
     */
    public String mostPopularMealsOverall();
}
