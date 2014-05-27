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
    public String mostPopularMealsOverall() {
        Set<OrderedMenuItemRecorder> orderedMenuItemRecorders = Sets.newHashSet();      // store for all of the records of items that have been ordered.

        for (RestaurantData restaurantData : findAll()) {

            Collection<Order> orders = getOrders(restaurantData);

            LoadingCache<Long, Optional<MenuItem>> cache = createMenuLoadingCache(restaurantData);

            Map<Long, OrderedMenuItemRecorder> idsToOrderedItemsRecorder = Maps.newHashMap();
            for (Order order : orders) {
                for (LineItem lineItem : order.getLineItems()) {

                    Optional<MenuItem> optionalMenuItem = cache.getUnchecked(lineItem.getId());

                    if (optionalMenuItem.isPresent()) {
                        MenuItem menuItem = optionalMenuItem.get();
                        OrderedMenuItemRecorder orderedMenuItemRecorder;
                        if (!idsToOrderedItemsRecorder.containsKey(menuItem.getId())) {

                            orderedMenuItemRecorder = new OrderedMenuItemRecorder()
                                    .setCategory(menuItem.getCategory())
                                    .setId(menuItem.getId())
                                    .setMealName(menuItem.getName())
                                    .setRestaurantId(restaurantData.getRestaurant());
                            idsToOrderedItemsRecorder.put(menuItem.getId(), orderedMenuItemRecorder);

                        } else {
                            orderedMenuItemRecorder = idsToOrderedItemsRecorder.get(menuItem.getId());
                        }
                        orderedMenuItemRecorder.addQuantity(lineItem.getQuantity());

                    } else {
                        Logger.info("Unable to find the menu item with the id %d", lineItem.getId());
                    }
                }
            }
            orderedMenuItemRecorders.addAll(idsToOrderedItemsRecorder.values());
        }
        return getOrderedJsonString(orderedMenuItemRecorders);
    }

    private String getOrderedJsonString(final Set<OrderedMenuItemRecorder> orderedMenuItemRecorders) {
        Collection<OrderedMenuItemRecorder> orderItems = Ordering.from(
                new Comparator<OrderedMenuItemRecorder>() {
                    @Override
                    public int compare(final OrderedMenuItemRecorder o1, final OrderedMenuItemRecorder o2) {
                        return o2.getQuantity() - o1.getQuantity();
                    }
                }
        ).sortedCopy(orderedMenuItemRecorders);
        return GSON.toJson(orderItems);
    }

    /**
     * Create a Loading Cache of type MenuId -> MenuItem for a restaurant. The MenuItem will be of type {@link Optional} since there are some ordered LineItems that don't have a
     * corresponding MenuItem
     *
     * @param restaurantData the Restaurant that the cache should be created for.
     * @return a cache that can be used to get a MenuItem from a restaurant by its ID.
     */
    private LoadingCache<Long, Optional<MenuItem>> createMenuLoadingCache(final RestaurantData restaurantData) {
        final Menu menu = getMenu(restaurantData);
        return CacheBuilder.newBuilder().build(
                new CacheLoader<Long, Optional<MenuItem>>() {
                    @Override
                    public Optional<MenuItem> load(final Long id) throws Exception {
                        for (List<MenuItem> menuItemList : menu.getMenu().values()) {
                            for (MenuItem menuItem : menuItemList) {
                                if (id.equals(menuItem.getId())) {
                                    return Optional.of(menuItem);
                                }
                            }
                        }
                        return Optional.absent();
                    }
                }
        );
    }

    private Menu getMenu(final RestaurantData restaurantData) {
        return GSON.fromJson(restaurantData.getMenuJson().getValue(), MENU_TYPE);
    }

    private Collection<Order> getOrders(final RestaurantData restaurantLoadData) {
        return GSON.fromJson(restaurantLoadData.getOrdersJson().getValue(), COLLECTION_TYPE);
    }

    /**
     * Used as a record for an Item on a menu and the number of times it has been ordered.
     */
    @VisibleForTesting
    static final class OrderedMenuItemRecorder {

        private long id;

        private String restaurantId;

        private String mealName;

        private int quantity;

        private String category;

        OrderedMenuItemRecorder() {
            this.quantity = 0;
        }

        long getId() {
            return id;
        }

        OrderedMenuItemRecorder setId(final long id) {
            this.id = id;
            return this;
        }

        String getRestaurantId() {
            return restaurantId;
        }

        OrderedMenuItemRecorder setRestaurantId(final String restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        String getMealName() {
            return mealName;
        }

        OrderedMenuItemRecorder setMealName(final String mealName) {
            this.mealName = mealName;
            return this;
        }

        int getQuantity() {
            return quantity;
        }

        void addQuantity(final int quantity) {
            this.quantity += quantity;
        }

        String getCategory() {
            return category;
        }

        OrderedMenuItemRecorder setCategory(final String category) {
            this.category = category;
            return this;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof OrderedMenuItemRecorder)) {
                return false;
            }

            final OrderedMenuItemRecorder that = (OrderedMenuItemRecorder) o;

            if (id != that.id) {
                return false;
            }
            return !(restaurantId != null ? !restaurantId.equals(that.restaurantId) : that.restaurantId != null);

        }

        @Override
        public int hashCode() {
            int result = (int) (id ^ (id >>> 32));
            result = 31 * result + (restaurantId != null ? restaurantId.hashCode() : 0);
            return result;
        }
    }
}
