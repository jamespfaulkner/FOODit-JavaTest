package com.foodit.test.sample.bi;

import com.foodit.test.sample.model.LineItem;
import com.foodit.test.sample.model.MenuItem;
import com.foodit.test.sample.model.RestaurantData;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author James Faulkner
 */
public class CategoryCountGeneratorTest {

    @Test
    public void testCreateRecorder() throws Exception {
        CategoryCountGenerator categoryCountGenerator = new CategoryCountGenerator();
        final LineItem lineItem = new LineItem().setQuantity(1);
        final RestaurantData restaurantData = new RestaurantData();
        restaurantData.setRestaurant("FooBar");
        final MenuItem menuItem = new MenuItem();
        menuItem.setCategory("FooBarCat");

        CategoryCounter counter = categoryCountGenerator.createRecorder(lineItem, restaurantData, menuItem);
        Assert.assertEquals("FooBarCat", counter.getCategory());
        Assert.assertEquals(0, counter.getQuantity());
    }

    @Test
    public void testGetUniqueId() throws Exception {
        CategoryCountGenerator categoryCountGenerator = new CategoryCountGenerator();
        final LineItem lineItem = new LineItem().setQuantity(1);
        final RestaurantData restaurantData = new RestaurantData();
        restaurantData.setRestaurant("FooBar");
        final MenuItem menuItem = new MenuItem();
        menuItem.setCategory("FooBarCat");

        String counterId = categoryCountGenerator.getUniqueId(lineItem, restaurantData, menuItem);
        Assert.assertEquals("FooBarCat", counterId);
    }
}
