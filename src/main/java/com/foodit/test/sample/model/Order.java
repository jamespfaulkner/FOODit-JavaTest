package com.foodit.test.sample.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author James Faulkner
 */
public class Order {

    private BigDecimal totalValue;
    private List<LineItem> lineItems;
    private String orderId;

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(final BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(final List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(final String orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }

        final Order order = (Order) o;

        if (orderId != null ? !orderId.equals(order.orderId) : order.orderId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return orderId != null ? orderId.hashCode() : 0;
    }
}
