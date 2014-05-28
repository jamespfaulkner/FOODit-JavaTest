package com.foodit.test.sample.bi;

/**
 * Implementation of {@link EntityCounter} for Order Items
 *
 * @author James Faulkner
 */
public class OrderedMenuItemCounter implements EntityCounter {

    private long id;

    private String restaurantId;

    private String mealName;

    // security exception when this is used by GSON in google app engine. do it the old fashioned, non thread safe way
    //private AtomicInteger quantity;
    private int quantity;

    private String category;

    public OrderedMenuItemCounter() {
        this.quantity = 0;
    }

    long getId() {
        return id;
    }

    public OrderedMenuItemCounter setId(final long id) {
        this.id = id;
        return this;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public OrderedMenuItemCounter setRestaurantId(final String restaurantId) {
        this.restaurantId = restaurantId;
        return this;
    }

    public String getMealName() {
        return mealName;
    }

    public OrderedMenuItemCounter setMealName(final String mealName) {
        this.mealName = mealName;
        return this;
    }

    @Override
    public String identifier() {
        return Long.toString(getId());
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(final int quantity) {
        this.quantity += quantity;
    }

    public String getCategory() {
        return category;
    }

    public OrderedMenuItemCounter setCategory(final String category) {
        this.category = category;
        return this;
    }

    /**
     * Equals is calculated on the meal {@link #id} and the {@link #restaurantId}, just in case the id is not unique across different restaurants
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderedMenuItemCounter)) {
            return false;
        }

        final OrderedMenuItemCounter that = (OrderedMenuItemCounter) o;

        if (id != that.id) {
            return false;
        }
        if (restaurantId != null ? !restaurantId.equals(that.restaurantId) : that.restaurantId != null) {
            return false;
        }

        return true;
    }

    /**
     * HashCode is calculated on the meal {@link #id} and the {@link #restaurantId}, just in case the id is not unique across different restaurants
     */
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (restaurantId != null ? restaurantId.hashCode() : 0);
        return result;
    }
}
