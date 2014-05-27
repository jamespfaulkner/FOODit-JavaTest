package com.foodit.test.sample.service;

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

import static com.foodit.test.sample.service.GsonRestaurantDataServiceImpl.OrderedMenuItemRecorder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author James Faulkner
 */
public class GsonRestaurantDataServiceImplTest {

    private static final Gson GSON = new Gson();
    private static final Type ORDERED_ITEM_RECORDER_TYPE = new TypeToken<Collection<OrderedMenuItemRecorder>>() {
    }.getType();

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

    @Test
    public void testMostPopularMealsOverall() throws Exception {
        final List<RestaurantData> restaurantDataLists = Lists.newArrayList();
        RestaurantDataService restaurantDataService = new GsonRestaurantDataServiceImpl() {
            @Override
            public Collection<RestaurantData> findAll() {
                return restaurantDataLists;
            }
        };

        assertThat(restaurantDataService.mostPopularMealsOverall(), Matchers.is("[]"));

        restaurantDataLists.add(new RestaurantData("foobar", "{}", "[]"));
        assertThat(restaurantDataService.mostPopularMealsOverall(), Matchers.is("[]"));

        restaurantDataLists.add(new RestaurantData("foobar", readFile("menu-one.json"), readFile("orders-one.json")));
        assertThat(restaurantDataService.mostPopularMealsOverall(), Matchers.not(Matchers.isEmptyOrNullString()));

        Collection<OrderedMenuItemRecorder> orderedMenuItemRecorders = GSON.fromJson(restaurantDataService.mostPopularMealsOverall(), ORDERED_ITEM_RECORDER_TYPE);
        assertThat(orderedMenuItemRecorders, Matchers.hasSize(1));
        assertThat(orderedMenuItemRecorders, Matchers.contains(new GsonRestaurantDataServiceImpl.OrderedMenuItemRecorder().setId(5).setRestaurantId("foobar")));

        restaurantDataLists.add(new RestaurantData("Orders Two", readFile("menu-two.json"), readFile("orders-two.json")));
        orderedMenuItemRecorders = GSON.fromJson(restaurantDataService.mostPopularMealsOverall(), ORDERED_ITEM_RECORDER_TYPE);
        assertThat(orderedMenuItemRecorders, Matchers.hasSize(3));
        assertThat(
                orderedMenuItemRecorders, Matchers.containsInAnyOrder(
                new OrderedMenuItemRecorder().setId(5).setRestaurantId("foobar"),
                new OrderedMenuItemRecorder().setId(5).setRestaurantId("Orders Two"),
                new OrderedMenuItemRecorder().setId(37).setRestaurantId("Orders Two")
        )
        );

        assertEquals(orderedMenuItemRecorders.iterator().next(), new OrderedMenuItemRecorder().setId(5).setRestaurantId("Orders Two"));
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
}
