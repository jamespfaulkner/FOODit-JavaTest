package com.foodit.test.sample.bi;

import com.foodit.test.sample.bi.service.BusinessIntelligenceServiceImpl;

/**
 * Interface that is used by the {@link BusinessIntelligenceServiceImpl} to collate information that is stored in the system. Normally used to count the number of times an entity
 * appears in the system, for example the number of Meals of a certain type that are in the system. They are normally grouped together (and the quantity correctly modified) using
 * the {@link #identifier()}.
 *
 * @author James Faulkner
 */
public interface EntityCounter {

    /**
     * The identifier that is used for this EntityCounter to uniquely identify the entity. For example, this might be the name of a category etc.
     *
     * @return the identify for this instance.
     */
    String identifier();

    /**
     * Return the number of times an entity appears in the system. Note that during processing, this will not be finished and hence will not be the final figure.
     *
     * @return the number of times an entity appears in the system.
     */
    int getQuantity();

    /**
     * Add a number to the quantity that is already present.<br/>
     * Note, it is up to the implementations of this class to decide whether this method is thread safe or not.
     *
     * @param quantity the quantity to add.
     */
    void addQuantity(final int quantity);
}
