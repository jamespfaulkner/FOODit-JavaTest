package com.foodit.test.sample.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import com.foodit.test.sample.model.RestaurantData;
import com.foodit.test.sample.service.RestaurantDataService;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.http.HttpSupport;
import com.threewks.thundr.view.jsp.JspView;
import com.threewks.thundr.view.string.StringView;

public class DataLoadController {

    @Inject
    private RestaurantDataService restaurantDataService;

    public JspView instructions() {
        return new JspView("instructions.jsp");
    }

    public StringView load() {
        restaurantDataService.initData();
        return new StringView("Data loaded.");
    }

    public void viewData(String restaurant, HttpServletResponse response) throws IOException {
        RestaurantData restaurantLoadData = restaurantDataService.get(restaurant);
        String data = restaurantLoadData.viewData();
        writeJsonResponse(response, data);
    }

    public void viewOrders(String restaurant, HttpServletResponse response) throws IOException {
        RestaurantData restaurantLoadData = restaurantDataService.get(restaurant);
        String data = restaurantLoadData.getOrdersJson().getValue();
        writeJsonResponse(response, data);
    }

    public void viewMenu(String restaurant, HttpServletResponse response) throws IOException {
        RestaurantData restaurantLoadData = restaurantDataService.get(restaurant);
        String data = restaurantLoadData.getMenuJson().getValue();
        writeJsonResponse(response, data);
    }

    private void writeJsonResponse(final HttpServletResponse response, final String data) throws IOException {
        response.addHeader(HttpSupport.Header.ContentType, ContentType.ApplicationJson.value());
        response.getWriter().write(data);
        response.setContentLength(data.getBytes().length);
    }
}
