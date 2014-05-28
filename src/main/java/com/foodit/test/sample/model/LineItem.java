package com.foodit.test.sample.model;

/**
 * @author James Faulkner
 */
public class LineItem {

    private long id;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public LineItem setQuantity(final int quantity) {
        this.quantity = quantity;
        return this;
    }

    public long getId() {
        return id;
    }

    public LineItem setId(final long id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "LineItem{" +
                "id=" + id +
                ", quantity=" + quantity +
                '}';
    }
}
