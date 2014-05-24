package com.foodit.test.sample.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.foodit.test.sample.dao.RestaurantDAO;
import com.foodit.test.sample.model.RestaurantData;
import com.google.appengine.repackaged.com.google.common.base.Function;
import com.google.appengine.repackaged.com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.threewks.thundr.logger.Logger;
import org.apache.commons.io.IOUtils;

/**
 * @author James Faulkner
 */
public abstract class AbstractRestaurantDataService implements RestaurantDataService {

    public static final List<String> RESTAURANTS = ImmutableList.of("bbqgrill", "butlersthaicafe", "jashanexquisiteindianfood", "newchinaexpress");

    @Inject
    private RestaurantDAO restaurantDAO;

    @Override
    public RestaurantData get(final String restaurantName) {
        return restaurantDAO.get(restaurantName);
    }

    @Override
    public void save(final RestaurantData restaurantData) {
        restaurantDAO.save(restaurantData);
    }

    @Override
    public Collection<RestaurantData> findAll() {
        return Collections2.transform(
                RESTAURANTS, new Function<String, RestaurantData>() {
            @Override
            public RestaurantData apply(final String restaurantName) {
                return loadData(restaurantName);
            }
        }
        );
    }

    @Override
    public void initData() {
        Logger.info("Loading data");
        for (String restaurant : RESTAURANTS) {
            restaurantDAO.save(loadData(restaurant));
        }
    }

    private RestaurantData loadData(String restaurantName) {
        String orders = readFile(String.format("orders-%s.json", restaurantName));
        String menu = readFile(String.format("menu-%s.json", restaurantName));
        return new RestaurantData(restaurantName, menu, orders);
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
