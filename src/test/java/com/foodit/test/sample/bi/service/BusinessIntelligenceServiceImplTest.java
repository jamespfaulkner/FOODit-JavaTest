package com.foodit.test.sample.bi.service;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import com.foodit.test.sample.bi.OrderedMenuItemCounter;
import com.foodit.test.sample.model.RestaurantData;
import com.foodit.test.sample.service.GsonRestaurantDataServiceImpl;
import com.foodit.test.sample.service.RestaurantDataService;
import com.foodit.utils.ReflectionUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.googlecode.objectify.repackaged.gentyref.TypeToken;
import org.hamcrest.Matchers;
import org.junit.Test;

import static com.foodit.utils.FileUtils.readFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author James Faulkner
 */
public class BusinessIntelligenceServiceImplTest {

    private static final Gson GSON = new Gson();
    private static final Type ORDERED_ITEM_RECORDER_TYPE = new TypeToken<Collection<OrderedMenuItemCounter>>() {
    }.getType();

    @Test
    public void testMostPopularMealsOverall() throws Exception {
        final List<RestaurantData> restaurantDataLists = Lists.newArrayList();
        RestaurantDataService restaurantDataService = new GsonRestaurantDataServiceImpl() {
            @Override
            public Collection<RestaurantData> findAll() {
                return restaurantDataLists;
            }
        };

        BusinessIntelligenceServiceImpl businessIntelligenceService = new BusinessIntelligenceServiceImpl();
        ReflectionUtils.set(businessIntelligenceService, "restaurantDataService", restaurantDataService);

        assertThat(businessIntelligenceService.mostPopularMeals(), Matchers.is("[]"));

        restaurantDataLists.add(new RestaurantData("foobar", "{}", "[]"));
        assertThat(businessIntelligenceService.mostPopularMeals(), Matchers.is("[]"));

        restaurantDataLists.add(new RestaurantData("foobar", readFile("menu-one.json"), readFile("orders-one.json")));
        assertThat(businessIntelligenceService.mostPopularMeals(), Matchers.not(Matchers.isEmptyOrNullString()));

        Collection<OrderedMenuItemCounter> orderedMenuItemRecorders = GSON.fromJson(businessIntelligenceService.mostPopularMeals(), ORDERED_ITEM_RECORDER_TYPE);
        assertThat(orderedMenuItemRecorders, Matchers.hasSize(1));
        assertThat(orderedMenuItemRecorders, Matchers.contains(new OrderedMenuItemCounter().setId(5).setRestaurantId("foobar")));

        restaurantDataLists.add(new RestaurantData("Orders Two", readFile("menu-two.json"), readFile("orders-two.json")));
        orderedMenuItemRecorders = GSON.fromJson(businessIntelligenceService.mostPopularMeals(), ORDERED_ITEM_RECORDER_TYPE);
        assertThat(orderedMenuItemRecorders, Matchers.hasSize(3));
        assertThat(
                orderedMenuItemRecorders, Matchers.containsInAnyOrder(
                new OrderedMenuItemCounter().setId(5).setRestaurantId("foobar"),
                new OrderedMenuItemCounter().setId(5).setRestaurantId("Orders Two"),
                new OrderedMenuItemCounter().setId(37).setRestaurantId("Orders Two")
        )
        );

        assertEquals(orderedMenuItemRecorders.iterator().next(), new OrderedMenuItemCounter().setId(5).setRestaurantId("Orders Two"));
    }

    @Test
    public void testMostPopularCategories() throws Exception {

    }

}
