package com.foodit.test.sample.bi;

import com.foodit.test.sample.model.LineItem;
import com.foodit.test.sample.model.MenuItem;
import com.foodit.test.sample.model.RestaurantData;

/**
 * @author James Faulkner
 */
public class OrderedMenuItemCountGenerator implements CountGenerator<OrderedMenuItemCounter> {

    @Override
    public OrderedMenuItemCounter createRecorder(final LineItem lineItem, final RestaurantData restaurantData, final MenuItem menuItem) {
        return new OrderedMenuItemCounter()
                .setCategory(menuItem.getCategory())
                .setId(menuItem.getId())
                .setMealName(menuItem.getName())
                .setRestaurantId(restaurantData.getRestaurant());
    }

    @Override
    public String getUniqueId(final LineItem lineItem, final RestaurantData restaurantData, final MenuItem menuItem) {
        return Long.toString(lineItem.getId());
    }
}
