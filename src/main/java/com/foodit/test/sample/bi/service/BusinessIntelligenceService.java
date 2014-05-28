package com.foodit.test.sample.bi.service;

/**
 * Service class that deals with analysing the data
 *
 * @author James Faulkner
 */
public interface BusinessIntelligenceService {

    /**
     * Generate a Json String of the most popular meals currently in the system.
     *
     * @return a Json String of the most popular meals currently in the system.
     */
    String mostPopularMeals();

    /**
     * Generate a Json String of the most popular meals for the supplied restaurant
     *
     * @return a Json String of the most popular meals currently in the system.
     */
    String mostPopularMeals(String restaurantName);

    /**
     * Generates a JSON String of the most popular categories currently in the system
     *
     * @return a JSON String of the most popular categories currently in the system
     */
    String mostPopularCategories();

    /**
     * Generates a JSON String of the most popular categories currently in the system.
     *
     * @return a JSON String of the most popular categories currently in the system.
     */
    String mostPopularCategories(String restaurantName);
}
