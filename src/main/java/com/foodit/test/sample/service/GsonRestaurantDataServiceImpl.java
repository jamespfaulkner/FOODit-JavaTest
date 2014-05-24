package com.foodit.test.sample.service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;

import com.foodit.test.sample.model.Order;
import com.foodit.test.sample.model.RestaurantData;
import com.google.gson.Gson;
import com.googlecode.objectify.repackaged.gentyref.TypeToken;

/**
 * @author James Faulkner
 * @since 4.8
 */
public class GsonRestaurantDataServiceImpl extends AbstractRestaurantDataService {

    @Override
    public long orderCount(final String restaurantName) {
        RestaurantData restaurantLoadData = get(restaurantName);
        Collection<Order> orders = getOrders(restaurantLoadData);
        return (long) orders.size();
    }

    @Override
    public double salesValue(final String restaurantName) {
        BigDecimal value = new BigDecimal(0L);
        for (Order order : getOrders(get(restaurantName))) {
            value = value.add(order.getTotalValue());
        }
        return value.doubleValue();
    }

    @Override
    public String mostPopularMealsOverall() {
        throw new UnsupportedOperationException();
    }

    private Collection<Order> getOrders(final RestaurantData restaurantLoadData) {
        Type collectionType = new TypeToken<Collection<Order>>() {}.getType();
        return new Gson().fromJson(restaurantLoadData.getOrdersJson().getValue(), collectionType);
    }
}
