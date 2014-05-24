package com.foodit.test.sample.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author James Faulkner
 * @since 4.8
 */
public class Order {

    private BigDecimal totalValue;
//    private List<LineItem> lineItems;

    public Order() {
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(final BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

//    public List<LineItem> getLineItems() {
//        return lineItems;
//    }
//
//    public void setLineItems(final List<LineItem> lineItems) {
//        this.lineItems = lineItems;
//    }
}
