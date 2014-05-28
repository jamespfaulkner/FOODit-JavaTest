package com.foodit.test.sample.bi;

import com.foodit.test.sample.model.LineItem;
import com.foodit.test.sample.model.MenuItem;
import com.foodit.test.sample.model.RestaurantData;

/**
 * Implementation of {@link CountGenerator} for {@link CategoryCounter}.
 *
 * @author James Faulkner
 */
public class CategoryCountGenerator implements CountGenerator<CategoryCounter> {

    @Override
    public CategoryCounter createRecorder(final LineItem lineItem, final RestaurantData restaurantData, final MenuItem menuItem) {
        return new CategoryCounter(menuItem.getCategory());
    }

    @Override
    public String getUniqueId(final LineItem lineItem, final RestaurantData restaurantData, final MenuItem menuItem) {
        return menuItem.getCategory();
    }
}