package com.foodit.test.sample.controller;

import java.text.DecimalFormat;

import javax.inject.Inject;

import com.foodit.test.sample.bi.service.BusinessIntelligenceService;
import com.foodit.test.sample.service.RestaurantDataService;
import com.threewks.thundr.view.string.StringView;

/**
 * @author James Faulkner
 */
public class RestApiController {

    @Inject
    private RestaurantDataService restaurantDataService;

    @Inject
    private BusinessIntelligenceService businessIntelligenceService;

    public StringView orders(String restaurantName) {
        return new StringView("" + restaurantDataService.orderCount(restaurantName));
    }

    public StringView sales(String restaurantName) {
        return new StringView(new DecimalFormat("#.00").format(restaurantDataService.salesValue(restaurantName)));
    }

    public StringView mostPopularMeals() {
        return new StringView(businessIntelligenceService.mostPopularMeals());
    }

    public StringView mostPopularCategories() {
        return new StringView(businessIntelligenceService.mostPopularCategories());
    }

    public StringView mostPopularMealsForRestaurant(String restaurantName) {
        return new StringView(businessIntelligenceService.mostPopularMeals(restaurantName));
    }

    public StringView mostPopularCategoriesForRestaurant(String restaurantName) {
        return new StringView(businessIntelligenceService.mostPopularCategories(restaurantName));
    }
}
