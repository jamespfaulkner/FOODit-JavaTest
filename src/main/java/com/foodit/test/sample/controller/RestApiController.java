package com.foodit.test.sample.controller;

import javax.inject.Inject;

import com.foodit.test.sample.service.RestaurantDataService;
import com.threewks.thundr.view.string.StringView;

/**
 * @author James Faulkner
 */
public class RestApiController {

    @Inject
    private RestaurantDataService restaurantDataService;

    public StringView orders(String restaurantName) {
        return new StringView("" + restaurantDataService.orderCount(restaurantName));
    }

    public StringView sales(String restaurantName) {
        return new StringView("" + restaurantDataService.salesValue(restaurantName));
    }

    public StringView mostPopularMeals() {
        return new StringView(restaurantDataService.mostPopularMealsOverall());
    }
}
