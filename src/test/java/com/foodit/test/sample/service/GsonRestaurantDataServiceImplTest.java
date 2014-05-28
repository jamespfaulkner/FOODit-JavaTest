package com.foodit.test.sample.service;

import com.foodit.test.sample.dao.RestaurantDAO;
import com.foodit.test.sample.mock.MockRestaurantDAO;
import com.foodit.test.sample.model.RestaurantData;
import com.foodit.utils.ReflectionUtils;
import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;
import org.junit.Test;

import static com.foodit.utils.FileUtils.readFile;
import static org.junit.Assert.assertEquals;

/**
 * @author James Faulkner
 */
public class GsonRestaurantDataServiceImplTest {

    private static final Gson GSON = new Gson();

    @Test
    public void testOrderCount() throws Exception {
        RestaurantDataService restaurantDataService = new GsonRestaurantDataServiceImpl();
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

        restaurantData.setOrdersJson(new Text(readFile("orders-three.json")));
        assertEquals(86L, restaurantDataService.orderCount("Bar Foo"));
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
}
