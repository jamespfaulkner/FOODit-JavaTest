package com.foodit.test.sample.model;

import java.util.List;
import java.util.Map;

/**
 * @author James Faulkner
 */
public class Menu {

    private Map<String, List<MenuItem>> menu;

    public Map<String, List<MenuItem>> getMenu() {
        return menu;
    }

    public void setMenu(final Map<String, List<MenuItem>> menu) {
        this.menu = menu;
    }
}
