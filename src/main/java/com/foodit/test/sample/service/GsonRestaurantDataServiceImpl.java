package com.foodit.test.sample.service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.foodit.test.sample.model.LineItem;
import com.foodit.test.sample.model.Menu;
import com.foodit.test.sample.model.MenuItem;
import com.foodit.test.sample.model.Order;
import com.foodit.test.sample.model.RestaurantData;
import com.google.appengine.repackaged.com.google.common.base.Optional;
import com.google.appengine.repackaged.com.google.common.collect.Maps;
import com.google.appengine.repackaged.com.google.common.collect.Ordering;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.googlecode.objectify.repackaged.gentyref.TypeToken;
import com.threewks.thundr.logger.Logger;

/**
 * @author James Faulkner
 */
public class GsonRestaurantDataServiceImpl extends AbstractRestaurantDataService {

    private static final Gson GSON = new Gson();
    private static final Type COLLECTION_TYPE = new TypeToken<Collection<Order>>() {
    }.getType();
    private static final Type MENU_TYPE = new TypeToken<Menu>() {
    }.getType();

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
    public Menu getMenu(final RestaurantData restaurantData) {
        return GSON.fromJson(restaurantData.getMenuJson().getValue(), MENU_TYPE);
    }

    @Override
    public Collection<Order> getOrders(final RestaurantData restaurantLoadData) {
        return GSON.fromJson(restaurantLoadData.getOrdersJson().getValue(), COLLECTION_TYPE);
    }
}
