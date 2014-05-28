package com.foodit.test.sample.bi;

/**
 * Implementation of {@link EntityCounter} for Categories
 *
 * @author James Faulkner
 */
public final class CategoryCounter
        implements EntityCounter {

    private final String category;

    // security exception when this is used by GSON in google app engine. do it the old fashioned, non thread safe way
    //private AtomicInteger quantity;

    private int quantity;

    public CategoryCounter(final String category) {
        this.category = category;
        quantity = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String identifier() {
        return category;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getQuantity() {
        return quantity;
    }

    /**
     * This version is thread safe.
     */
    @Override
    public void addQuantity(final int quantity) {
        this.quantity += quantity;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryCounter)) {
            return false;
        }

        final CategoryCounter that = (CategoryCounter) o;

        if (category != null ? !category.equals(that.category) : that.category != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return category != null ? category.hashCode() : 0;
    }
}