package com.foodit.test.sample.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.foodit.test.sample.dao.RestaurantDAO;
import com.foodit.test.sample.exception.RestaurantDataException;
import com.foodit.test.sample.model.RestaurantData;
import com.google.appengine.repackaged.com.google.common.base.Function;
import com.google.appengine.repackaged.com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.threewks.thundr.logger.Logger;
import org.apache.commons.io.IOUtils;

/**
 * Implementation of {@link RestaurantDataService} that uses javascript to do the work. <br/> This is far from teh best way of doing this, but is an easy way of getting it up and
 * running quickly due to the ease of using JSON in Javascript and the built in map/reduce functions.
 *
 * @author James Faulkner
 */
public class RhinoRestaurantDataServiceImpl extends AbstractRestaurantDataService {

    @Override
    public long orderCount(final String restaurantName) {
        RestaurantData restaurantLoadData = get(restaurantName);
        final String script = "var numOfOrders = " + restaurantLoadData.getOrdersJson().getValue() + ".length;";
        Object numOfOrders = runJavascript(script, "numOfOrders");
        return (long) Double.parseDouble(numOfOrders.toString());
    }

    public double salesValue(final String restaurantName) {
        RestaurantData restaurantLoadData = get(restaurantName);
        final String script =
                "var orders = " + restaurantLoadData.getOrdersJson().getValue() + ";" +
                        "var totalSales = 0;" +
                        "for(var i = 0; i < orders.length; i++)" +
                        "   totalSales += parseInt(orders[i].totalValue)";

        Object numOfOrders = runJavascript(script, "totalSales");
        return Double.parseDouble(numOfOrders.toString());
    }

    public String mostPopularMealsOverall() {
        StringBuilder script = new StringBuilder("var orders = [];");
        for (RestaurantData restaurantData : findAll()) {
            script.append("orders = orders.concat(").append(restaurantData.getOrdersJson().getValue()).append(");\n");
        }
        script.append(
                "var orderIdsToNumOfOrders = orders.map(function(current, b, c){\n" +
                        "  var arr = [];  \n" +
                        "  var storeId = current.storeId;" +
                        "  for(var i = 0; i < current.lineItems.length; i++) { \n" +
                        "    if(typeof current.lineItems[i].id !== 'undefined') {" +
                        "      arr.push(storeId + '|' + current.lineItems[i].id);  " +
                        "    }" +
                        "  }  " +
                        "  return arr;  \n" +
                        "}) \n" +
                        ".reduce(function(previous, current) { \n" +
                        "    current.forEach(function(itemId) { \n" +
                        "      if (typeof previous[itemId] !== \"undefined\") { \n" +
                        "          previous[itemId]++; \n" +
                        "      } else { \n" +
                        "          previous[itemId] = 1; \n" +
                        "      } \n" +
                        "    }); \n" +
                        "    return previous; \n" +
                        "  }, {});\n"
        );
        script.append(
                "var orderIdsToNumOfOrdersArr = [];\n" +
                        "for (var key in orderIdsToNumOfOrders) {\n" +
                        "  if (orderIdsToNumOfOrders .hasOwnProperty(key)) {\n" +
                        "    var mealToNumOrders = {\"id\" : 0, \"numOfOrders\" : 0};\n" +
                        "    mealToNumOrders.id = key;\n" +
                        "    mealToNumOrders.numOfOrders = orderIdsToNumOfOrders[key];\n" +
                        "    orderIdsToNumOfOrdersArr.push(mealToNumOrders)\n" +
                        "  }\n" +
                        "};\n" +
                        "var sortedResult = orderIdsToNumOfOrdersArr.sort(function(first, second){return second.numOfOrders-first.numOfOrders});\n" +

                        "var jsonString = '[';" +
                        "for (var i = 0; i < sortedResult.length; i++) { \n" +
                        "  jsonString += '{\"storeId\" : \"' + sortedResult[i].id.split('|')[0] + '\",\"mealId\" : \"' + sortedResult[i].id.split('|')[1] + '\"}';\n" +
                        "    if (i < sortedResult.length-1) " +
                        "      jsonString += ','" +
                        "}" +
                        "jsonString += ']';"
        );

        return "" + runJavascript(script.toString(), "jsonString");
    }

    private Object runJavascript(String script, String variable) {
        try {
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("JavaScript");
            engine.eval(script);
            return engine.get(variable);
        } catch (Exception e) {
            throw new RestaurantDataException("There was a problem running the script", e);
        }
    }
}
