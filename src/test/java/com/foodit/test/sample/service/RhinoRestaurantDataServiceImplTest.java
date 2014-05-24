package com.foodit.test.sample.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import com.foodit.test.sample.dao.RestaurantDAO;
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
    public void testSalesValue() {
        final List<RestaurantData> restaurantDataLists = Lists.newArrayList();
        RestaurantDataService restaurantDataService = new RhinoRestaurantDataServiceImpl() {
            @Override
            public Collection<RestaurantData> findAll() {
                return restaurantDataLists;
            }
        };

        assertThat(restaurantDataService.mostPopularMealsOverall(), Matchers.is("[]"));

        restaurantDataLists.add(new RestaurantData("Foo Bar", "[]", "[]"));
        assertThat(restaurantDataService.mostPopularMealsOverall(), Matchers.is("[]"));

        restaurantDataLists.add(new RestaurantData("Orders One", "[]", readFile("orders-one.json")));
        assertThat(restaurantDataService.mostPopularMealsOverall(), Matchers.not(Matchers.isEmptyOrNullString()));

        restaurantDataLists.add(new RestaurantData("Orders Two", "[]", readFile("orders-two.json")));

        Type collectionType = new TypeToken<Collection<Meal>>(){}.getType();
        Collection<Meal> meals = new Gson().fromJson(restaurantDataService.mostPopularMealsOverall(), collectionType);

        assertThat(meals, Matchers.hasSize(2));
        assertThat(meals, Matchers.contains(new Meal("bbqgrill", "5"), new Meal("bbqgrill", "37")));
    }

    private String readFile(String resourceName) {
        URL url = Resources.getResource(resourceName);
        try {
            return IOUtils.toString(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            Logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
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

    private static class MockRestaurantDAO implements RestaurantDAO {

        @Override
        public RestaurantData get(final String restaurantName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void save(final RestaurantData restaurantData) {
            throw new UnsupportedOperationException();
        }
    }
}
