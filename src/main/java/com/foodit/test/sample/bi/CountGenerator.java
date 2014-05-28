package com.foodit.test.sample.bi;

import com.foodit.test.sample.model.LineItem;
import com.foodit.test.sample.model.MenuItem;
import com.foodit.test.sample.model.RestaurantData;

/**
 * This is used to create an instance of {@link EntityCounter} for a LineItem.
 *
 * @author James Faulkner
 */
public interface CountGenerator<T extends EntityCounter> {

    /**
     * Create an {@link EntityCounter} for a {@link LineItem}. Note that the result will not reflect the quantity present in the LineItem.
     *
     * @param lineItem       the LineItem that the EntityCounter should be created for
     * @param restaurantData the restaurantData that the LineItem is for
     * @param menuItem       the MenuItem that the LineItem is for
     * @return a new EntityCounter for that LineItem
     */
    public T createRecorder(LineItem lineItem, RestaurantData restaurantData, MenuItem menuItem);

    /**
     * Get the unique id that should be used for a {@link LineItem}s {@link EntityCounter}
     *
     * @param lineItem       the LineItem that the EntityCounter should be created for
     * @param restaurantData the restaurantData that the LineItem is for
     * @param menuItem       the MenuItem that the LineItem is for
     * @return a new EntityCounter for that LineItem
     */
    public String getUniqueId(LineItem lineItem, RestaurantData restaurantData, MenuItem menuItem);
}
