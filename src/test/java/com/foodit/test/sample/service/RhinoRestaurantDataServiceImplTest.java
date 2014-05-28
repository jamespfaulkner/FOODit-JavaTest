package com.foodit.test.sample.service;

import static com.foodit.utils.FileUtils.readFile;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import com.foodit.test.sample.dao.RestaurantDAO;
import com.foodit.test.sample.mock.MockRestaurantDAO;
import com.foodit.test.sample.model.RestaurantData;
import com.foodit.utils.ReflectionUtils;
import com.google.appengine.api.datastore.Text;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.googlecode.objectify.repackaged.gentyref.TypeToken;
import com.threewks.thundr.logger.Logger;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author James Faulkner
 */
public class RhinoRestaurantDataServiceImplTest {

    public void setUp() throws Exception {
    }

    public void tearDown() throws Exception {

    }

    @Test
    public void orderCountTest() {
        RestaurantDataService restaurantDataService = new RhinoRestaurantDataServiceImpl();
        final RestaurantData restaurantData = new RestaurantData("Bar Foo", "[]", "[]");
        RestaurantDAO dao = new MockRestaurantDAO() {
            public RestaurantData get(final String restaurantName) {
                return restaurantData;
            }
        };
        ReflectionUtils.set(restaurantDataService, "restaurantDAO", dao);

        assertEquals(0L, restaurantDataService.orderCount("Bar Foo"));

        restaurantData.setOrdersJson(new Text(readFile("orders-one.json")));
        assertEquals(1L, restaurantDataService.orderCount("Bar Foo"));

        restaurantData.setOrdersJson(new Text(readFile("orders-two.json")));
        assertEquals(2L, restaurantDataService.orderCount("Bar Foo"));
    }

    @Test
    public void testSalesValue() throws Exception {
        RestaurantDataService restaurantDataService = new GsonRestaurantDataServiceImpl();
        final RestaurantData restaurantData = new RestaurantData("Bar Foo", "[]", "[]");
        RestaurantDAO dao = new MockRestaurantDAO() {
            public RestaurantData get(final String restaurantName) {
                return restaurantData;
            }
        };
        ReflectionUtils.set(restaurantDataService, "restaurantDAO", dao);

        assertEquals(0D, restaurantDataService.salesValue("Foo Bar"), 0D);

        restaurantData.setOrdersJson(new Text(readFile("orders-one.json")));
        assertEquals(9.5D, restaurantDataService.salesValue("Foo Bar"), 0D);

        restaurantData.setOrdersJson(new Text(readFile("orders-two.json")));
        assertEquals(19D, restaurantDataService.salesValue("Foo Bar"), 0D);

        restaurantData.setOrdersJson(new Text(readFile("orders-three.json")));
        assertEquals(1497D, restaurantDataService.salesValue("Foo Bar"), 0D);
    }

    private class Meal {
        String storeId;
        String mealId;

        private Meal(final String storeId, final String mealId) {
            this.storeId = storeId;
            this.mealId = mealId;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Meal)) {
                return false;
            }

            final Meal meal = (Meal) o;

            if (mealId != null ? !mealId.equals(meal.mealId) : meal.mealId != null) {
                return false;
            }
            if (storeId != null ? !storeId.equals(meal.storeId) : meal.storeId != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = storeId != null ? storeId.hashCode() : 0;
            result = 31 * result + (mealId != null ? mealId.hashCode() : 0);
            return result;
        }
    }

}
