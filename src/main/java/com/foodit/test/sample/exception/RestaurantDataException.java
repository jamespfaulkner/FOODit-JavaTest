package com.foodit.test.sample.exception;

/**
 * @author James Faulkner
 */
public class RestaurantDataException extends RuntimeException {

    public RestaurantDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
